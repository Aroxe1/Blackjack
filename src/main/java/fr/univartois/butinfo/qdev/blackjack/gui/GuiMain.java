package fr.univartois.butinfo.qdev.blackjack.gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Point d'entrée de la version graphique du Blackjack.
 */
public final class GuiMain {

    private GuiMain() {
        // Classe utilitaire : pas d'instance.
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                    | UnsupportedLookAndFeelException e) {
                // On garde simplement le look and feel par défaut.
            }
            new BlackjackWindow().setVisible(true);
        });
    }
}
