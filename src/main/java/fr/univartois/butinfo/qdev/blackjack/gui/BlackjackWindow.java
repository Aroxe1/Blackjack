package fr.univartois.butinfo.qdev.blackjack.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import fr.univartois.butinfo.qdev.blackjack.game.BlackjackGame;
import fr.univartois.butinfo.qdev.blackjack.House;
import fr.univartois.butinfo.qdev.blackjack.Player;
import fr.univartois.butinfo.qdev.blackjack.Result;

/**
 * Interface graphique du Blackjack : une table, la main de la maison, celle de
 * chaque joueur, et les boutons pour tirer ou rester.
 */
public class BlackjackWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Color TABLE_TOP = new Color(0x0E, 0x5A, 0x33);
    private static final Color TABLE_BOTTOM = new Color(0x06, 0x30, 0x1C);
    private static final Color GOLD = new Color(0xFF, 0xD5, 0x4F);

    private final JLabel status = new JLabel(" ", SwingConstants.CENTER);
    private final JButton hitButton = new JButton("Tirer");
    private final JButton standButton = new JButton("Rester");
    private final JButton newRoundButton = new JButton("Nouvelle partie");
    private final JButton playersButton = new JButton("Joueurs…");
    private final JPanel table = createTablePanel();
    private final List<HandPanel> playerPanels = new ArrayList<>();

    private List<String> names = new ArrayList<>(List.of("Joueur"));
    private BlackjackGame game;
    private HandPanel housePanel;
    private int currentPlayer;
    private boolean roundOver;

    public BlackjackWindow() {
        super("Blackjack");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(760, 560));

        JScrollPane scroll = new JScrollPane(new CenteringPanel(table),
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        setContentPane(new BackgroundPanel());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(createControlBar(), BorderLayout.SOUTH);

        hitButton.addActionListener(event -> onHit());
        standButton.addActionListener(event -> onStand());
        newRoundButton.addActionListener(event -> startRound());
        playersButton.addActionListener(event -> onChangePlayers());

        pack();
        setLocationRelativeTo(null);
        startRound();
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        return panel;
    }

    private JPanel createControlBar() {
        status.setForeground(Color.WHITE);
        status.setFont(status.getFont().deriveFont(Font.BOLD, 16f));
        status.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttons.setOpaque(false);
        for (JButton button : List.of(hitButton, standButton, newRoundButton, playersButton)) {
            button.setFocusPainted(false);
            button.setFont(button.getFont().deriveFont(Font.BOLD, 14f));
            buttons.add(button);
        }

        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(12, 16, 16, 16));
        bar.add(status, BorderLayout.NORTH);
        bar.add(buttons, BorderLayout.CENTER);
        return bar;
    }

    /**
     * Distribue une nouvelle donne avec les joueurs courants.
     */
    private void startRound() {
        game = new BlackjackGame();
        for (String name : names) {
            game.addPlayer(name);
        }

        table.removeAll();
        playerPanels.clear();
        housePanel = new HandPanel(game.getHouse());
        housePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        table.add(housePanel);
        table.add(Box.createVerticalStrut(18));
        for (Player player : game.getPlayers()) {
            HandPanel panel = new HandPanel(player);
            panel.setAlignmentX(Component.CENTER_ALIGNMENT);
            playerPanels.add(panel);
            table.add(panel);
            table.add(Box.createVerticalStrut(8));
        }

        currentPlayer = 0;
        roundOver = false;
        if (runSafely(game::deal)) {
            refresh();
        }
        table.revalidate();
        table.repaint();
    }

    private void onHit() {
        Player player = game.getPlayers().get(currentPlayer);
        if (!runSafely(() -> game.hit(player))) {
            return;
        }
        if (player.isBusted()) {
            nextPlayer();
        }
        refresh();
    }

    private void onStand() {
        nextPlayer();
        refresh();
    }

    /**
     * Passe au joueur suivant, ou termine la donne si tous ont joué.
     */
    private void nextPlayer() {
        currentPlayer++;
        if (currentPlayer >= game.getPlayers().size()) {
            currentPlayer = game.getPlayers().size() - 1;
            roundOver = true;
            if (runSafely(game::playHouse)) {
                game.settle();
            }
        }
    }

    private void onChangePlayers() {
        String input = JOptionPane.showInputDialog(this,
                "Nom des joueurs, séparés par des virgules :", String.join(", ", names));
        if (input == null) {
            return;
        }
        List<String> newNames = new ArrayList<>();
        for (String name : input.split(",")) {
            if (!name.isBlank()) {
                newNames.add(name.trim());
            }
        }
        if (newNames.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Il faut au moins un joueur.", "Blackjack",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        names = newNames;
        startRound();
    }

    /**
     * Met à jour toutes les mains, le message d'état et les boutons.
     */
    private void refresh() {
        House house = game.getHouse();
        housePanel.refresh(roundOver ? describeHand(house.getHandValue(), house.isBusted())
                : "une carte cachée");
        housePanel.setActive(false);

        List<Player> players = game.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            HandPanel panel = playerPanels.get(i);
            panel.refresh(describe(player));
            panel.setActive(!roundOver && i == currentPlayer);
        }

        status.setText(statusText());
        status.setForeground(roundOver ? GOLD : Color.WHITE);
        hitButton.setEnabled(!roundOver);
        standButton.setEnabled(!roundOver);

        table.revalidate();
        table.repaint();
    }

    private String describeHand(int value, boolean busted) {
        return value + " points" + (busted ? " — dépassé !" : "");
    }

    private String describe(Player player) {
        String text = describeHand(player.getHandValue(), player.isBusted());
        Result result = player.getResult();
        if (roundOver && result != null) {
            text = text + "   →   " + result.getMessage();
        }
        return text;
    }

    private String statusText() {
        if (!roundOver) {
            return "Au tour de " + game.getPlayers().get(currentPlayer).getName();
        }
        List<String> parts = new ArrayList<>();
        for (Player player : game.getPlayers()) {
            Result result = player.getResult();
            parts.add(player.getName() + " " + (result == null ? "" : result.getMessage()));
        }
        return String.join("   |   ", parts);
    }

    /**
     * Exécute une étape du jeu en signalant proprement un paquet épuisé.
     *
     * @return {@code true} si l'étape s'est déroulée normalement
     */
    private boolean runSafely(Runnable step) {
        try {
            step.run();
            return true;
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this, "Le paquet est vide : la partie s'arrête ici.",
                    "Blackjack", JOptionPane.WARNING_MESSAGE);
            roundOver = true;
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            return false;
        }
    }

    /**
     * Centre un composant horizontalement (toujours) et verticalement (quand la place
     * disponible le permet) à l'intérieur d'un {@link JScrollPane}, tout en laissant
     * apparaître un ascenseur vertical si le contenu déborde.
     */
    private static class CenteringPanel extends JPanel implements Scrollable {

        private static final long serialVersionUID = 1L;

        private final JComponent content;

        CenteringPanel(JComponent content) {
            super(new GridBagLayout());
            this.content = content;
            setOpaque(false);
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.anchor = GridBagConstraints.CENTER;
            add(content, constraints);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return content.getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return visibleRect.height;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            if (getParent() instanceof JViewport viewport) {
                return content.getPreferredSize().height <= viewport.getHeight();
            }
            return false;
        }
    }

    /**
     * Fond de fenêtre imitant le tapis d'une table de casino.
     */
    private static class BackgroundPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(new GradientPaint(0, 0, TABLE_TOP, 0, getHeight(), TABLE_BOTTOM));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }
}
