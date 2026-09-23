package fr.univartois.butinfo.qdev.blackjack.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import fr.univartois.butinfo.qdev.blackjack.Card;
import fr.univartois.butinfo.qdev.blackjack.CardOwner;

/**
 * Affiche la main d'un joueur (ou de la maison) : son nom, ses cartes et son total.
 */
public class HandPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color ACTIVE = new Color(0xFF, 0xD5, 0x4F);
    private static final Color IDLE = new Color(0xFF, 0xFF, 0xFF, 60);

    private final CardOwner owner;
    private final JLabel title = new JLabel("", SwingConstants.CENTER);
    private final JLabel status = new JLabel("", SwingConstants.CENTER);
    private final JPanel cards = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));

    public HandPanel(CardOwner owner) {
        super(new BorderLayout(0, 4));
        this.owner = owner;
        setOpaque(false);
        setAlignmentX(Component.CENTER_ALIGNMENT);

        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        status.setForeground(new Color(0xE0, 0xE0, 0xE0));
        status.setFont(status.getFont().deriveFont(14f));
        status.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.add(title);
        header.add(status);

        cards.setOpaque(false);

        add(header, BorderLayout.NORTH);
        add(cards, BorderLayout.CENTER);
        setActive(false);
    }

    @Override
    public Dimension getMaximumSize() {
        // Empêche BoxLayout d'étirer la main sur toute la largeur/hauteur disponible,
        // ce qui permet à la table de la centrer horizontalement.
        return getPreferredSize();
    }

    /**
     * Met en évidence la main dont c'est le tour.
     */
    public final void setActive(boolean active) {
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, active ? ACTIVE : IDLE),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        title.setForeground(active ? ACTIVE : Color.WHITE);
    }

    /**
     * Redessine les cartes et le total à partir de l'état courant du propriétaire.
     *
     * @param statusText texte affiché à côté du nom (total, résultat, ...)
     */
    public void refresh(String statusText) {
        title.setText(owner.getName());
        status.setText(statusText);
        cards.removeAll();
        for (Card card : owner.getHand()) {
            cards.add(new CardView(card));
        }
        cards.revalidate();
        cards.repaint();
    }
}
