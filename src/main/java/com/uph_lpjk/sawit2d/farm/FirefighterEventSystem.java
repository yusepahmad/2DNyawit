package com.uph_lpjk.sawit2d.farm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.utility.AssetLoader;

public class FirefighterEventSystem {
    private static final int FIREFIGHTER_COST = 30;
    private final AssetLoader assetLoader = new AssetLoader();
    private final BufferedImage fireIcon = assetLoader.loadImage(40, 40, "assets/elephant-front.png", "../assets/elephant-front.png", "assets/elephant-back.png", "/tile/hut.png");

    public FirefighterResponse promptHelp(GamePanel gp, GameState state) {
        int option = showFirefighterDialog(gp);

        if (option == 0) {
            if (gp.getPlayerGold() < FIREFIGHTER_COST) {
                return new FirefighterResponse("Tim gajah pemadam batal berangkat. Gold tidak cukup. Nyaawit bisa nangani bencana gabisa, dasar miskin",
                        FarmBurnHandledType.NONE);
            }
            gp.setPlayerGold(-FIREFIGHTER_COST);
            state.modifyRisk(-3);
            state.addReputation(2);
            return new FirefighterResponse("Tim gajah pemadam tiba, api lebih cepat terkendali. Gold -" + FIREFIGHTER_COST + ".",
                    FarmBurnHandledType.FIREFIGHTER);
        } else if (option == 1) {
            state.modifyRisk(2);
            state.addReputation(-1);
            return new FirefighterResponse("Kamu memadamkan api sendiri. Risiko naik sedikit.",
                    FarmBurnHandledType.NONE);
        }
        return new FirefighterResponse("Bantuan pemadam diabaikan. Api masih mengancam kebun.",
                FarmBurnHandledType.NONE);
    }

    private int showFirefighterDialog(GamePanel gp) {
        AtomicInteger result = new AtomicInteger(-1);
        Window owner = SwingUtilities.getWindowAncestor(gp);
        JDialog dialog = new JDialog(owner instanceof Frame ? (Frame) owner : null, "Tim Gajah Pemadam", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setUndecorated(true);
        dialog.setResizable(false);

        JPanel outer = new JPanel(new BorderLayout());
        // outer.setBackground(new Color(10, 14, 20));
        // outer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        outer.setBackground(new Color(32, 24, 18));
        outer.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JPanel card = new JPanel(new BorderLayout(0, 16));
        // card.setBackground(new Color(20, 28, 40));
        // card.setBorder(BorderFactory.createCompoundBorder(
        //         BorderFactory.createLineBorder(new Color(255, 255, 255, 28), 1),
        //         BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        card.setBackground(new Color(44, 34, 26));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(214, 166, 82, 180), 2),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)));

        JPanel header = new JPanel(new BorderLayout(14, 0));
        header.setOpaque(false);
        JPanel icon = new JPanel(new BorderLayout());
        icon.setOpaque(false);
        icon.setPreferredSize(new java.awt.Dimension(52, 52));
        icon.setBackground(new Color(0, 0, 0, 0));
        // icon.setBorder(BorderFactory.createCompoundBorder(
        //         BorderFactory.createLineBorder(new Color(255, 255, 255, 28), 1),
        //         BorderFactory.createEmptyBorder(6, 6, 6, 6)));
        icon.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(214, 166, 82, 160), 2),
            BorderFactory.createEmptyBorder(6, 6, 6, 6)));

        JLabel iconLabel = new JLabel(new javax.swing.ImageIcon(fireIcon));
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        icon.add(iconLabel, BorderLayout.CENTER);

        JLabel title = new JLabel("Tim Gajah Pemadam Siaga");
        // title.setForeground(Color.WHITE);
        // title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(255, 240, 200));
        title.setFont(new Font("Monospaced", Font.BOLD, 20));

        JTextArea body = new JTextArea(
                "Asap masih naik dari petak kebun.\n" +
                "Pilih apakah kamu mau memanggil tim gajah pemadam atau menangani api sendiri.");
        body.setEditable(false);
        body.setFocusable(false);
        body.setOpaque(false);
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        // body.setForeground(new Color(232, 238, 245));
        // body.setFont(new Font("SansSerif", Font.PLAIN, 15));
        body.setForeground(new Color(245, 220, 180));
        body.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JLabel note = new JLabel("Biaya bantuan: " + FIREFIGHTER_COST + " gold");
        // note.setForeground(new Color(255, 198, 109));
        // note.setFont(new Font("SansSerif", Font.BOLD, 13));
        note.setForeground(new Color(226, 186, 116));
        note.setFont(new Font("Monospaced", Font.BOLD, 12));

        JPanel buttonRow = new JPanel(new GridBagLayout());
        buttonRow.setOpaque(false);

        // JButton callButton = createButton("Panggil tim gajah", new Color(48, 112, 220), new Color(64, 133, 255));
        // JButton handleButton = createButton("Tangani sendiri", new Color(80, 90, 102), new Color(98, 112, 128));
        JButton callButton = createButton("Panggil tim gajah", new Color(92, 64, 22), new Color(120, 84, 32));
        JButton handleButton = createButton("Tangani sendiri", new Color(60, 54, 44), new Color(82, 74, 60));

        ActionListener callAction = e -> {
            result.set(0);
            dialog.dispose();
        };
        ActionListener handleAction = e -> {
            result.set(1);
            dialog.dispose();
        };
        callButton.addActionListener(callAction);
        handleButton.addActionListener(handleAction);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 12);
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonRow.add(callButton, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        buttonRow.add(handleButton, gbc);

        JPanel headerText = new JPanel();
        headerText.setOpaque(false);
        headerText.setLayout(new javax.swing.BoxLayout(headerText, javax.swing.BoxLayout.Y_AXIS));
        headerText.add(title);
        headerText.add(javax.swing.Box.createVerticalStrut(8));
        headerText.add(body);

        header.add(icon, BorderLayout.WEST);
        header.add(headerText, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout(0, 8));
        footer.setOpaque(false);
        footer.add(note, BorderLayout.WEST);
        footer.add(buttonRow, BorderLayout.EAST);

        card.add(header, BorderLayout.NORTH);
        card.add(footer, BorderLayout.SOUTH);

        outer.add(card, BorderLayout.CENTER);
        dialog.setContentPane(outer);
        dialog.pack();
        dialog.setSize(620, 300);
        dialog.setLocationRelativeTo(gp);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);

        return result.get();
    }

    private JButton createButton(String text, Color base, Color hover) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(base);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        button.setMinimumSize(new java.awt.Dimension(180, 42));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(base);
            }
        });
        return button;
    }
}
