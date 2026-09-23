package fr.univartois.butinfo.qdev.blackjack.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;

import fr.univartois.butinfo.qdev.blackjack.Card;
import fr.univartois.butinfo.qdev.blackjack.Suit;

/**
 * Dessine une carte à jouer, face visible ou face cachée.
 */
public class CardView extends JComponent {

    private static final long serialVersionUID = 1L;

    /** Dimensions d'une carte, au ratio d'une vraie carte à jouer. */
    private static final int WIDTH = 84;
    private static final int HEIGHT = 120;
    private static final int ARC = 14;

    private static final Color RED = new Color(0xC0, 0x28, 0x28);
    private static final Color BLACK = new Color(0x22, 0x22, 0x22);
    private static final Color BACK = new Color(0x1D, 0x3E, 0x7A);
    private static final Color BACK_PATTERN = new Color(0x2E, 0x59, 0xA8);

    private final Card card;

    public CardView(Card card) {
        this.card = card;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setToolTipText(card.isFaceUp() ? card.toString() : "Carte cachée");
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        RoundRectangle2D shape = new RoundRectangle2D.Double(1, 1, WIDTH - 3.0, HEIGHT - 3.0, ARC, ARC);

        // Ombre portée.
        g2.setColor(new Color(0, 0, 0, 60));
        g2.fill(new RoundRectangle2D.Double(3, 4, WIDTH - 3.0, HEIGHT - 3.0, ARC, ARC));

        if (card.isFaceUp()) {
            paintFace(g2, shape);
        } else {
            paintBack(g2, shape);
        }

        g2.setColor(new Color(0x9E, 0x9E, 0x9E));
        g2.setStroke(new BasicStroke(1.5f));
        g2.draw(shape);

        g2.dispose();
    }

    private void paintFace(Graphics2D g2, RoundRectangle2D shape) {
        g2.setColor(Color.WHITE);
        g2.fill(shape);

        String label = card.getValue().getLabel();
        String symbol = card.getSuit().getSymbol();
        g2.setColor(isRed(card.getSuit()) ? RED : BLACK);

        // Coin supérieur gauche : valeur puis enseigne.
        g2.setFont(font(Font.BOLD, 20));
        g2.drawString(label, 8, 26);
        g2.setFont(font(Font.PLAIN, 16));
        g2.drawString(symbol, 8, 43);

        // Grande enseigne au centre.
        g2.setFont(font(Font.PLAIN, 44));
        drawCentered(g2, symbol, WIDTH / 2.0, HEIGHT / 2.0 + 8);

        // Coin inférieur droit, à l'envers.
        Graphics2D flipped = (Graphics2D) g2.create();
        flipped.rotate(Math.PI, WIDTH / 2.0, HEIGHT / 2.0);
        flipped.setFont(font(Font.BOLD, 20));
        flipped.drawString(label, 8, 26);
        flipped.setFont(font(Font.PLAIN, 16));
        flipped.drawString(symbol, 8, 43);
        flipped.dispose();
    }

    private void paintBack(Graphics2D g2, RoundRectangle2D shape) {
        g2.setColor(BACK);
        g2.fill(shape);
        g2.setClip(shape);
        g2.setColor(BACK_PATTERN);
        g2.setStroke(new BasicStroke(2f));
        for (int i = -HEIGHT; i < WIDTH + HEIGHT; i += 10) {
            g2.drawLine(i, 0, i + HEIGHT, HEIGHT);
            g2.drawLine(i, HEIGHT, i + HEIGHT, 0);
        }
        g2.setClip(null);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2f));
        g2.draw(new RoundRectangle2D.Double(7, 7, WIDTH - 15.0, HEIGHT - 15.0, ARC - 4, ARC - 4));
    }

    private void drawCentered(Graphics2D g2, String text, double centerX, double baselineY) {
        FontRenderContext context = g2.getFontRenderContext();
        Rectangle2D bounds = g2.getFont().getStringBounds(text, context);
        g2.drawString(text, (float) (centerX - bounds.getWidth() / 2), (float) baselineY);
    }

    private static boolean isRed(Suit suit) {
        return suit == Suit.HEARTS || suit == Suit.DIAMONDS;
    }

    private static Font font(int style, int size) {
        return new Font(Font.SANS_SERIF, style, size);
    }
}
