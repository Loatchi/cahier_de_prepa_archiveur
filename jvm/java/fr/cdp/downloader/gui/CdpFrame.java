package fr.cdp.downloader.gui;

import fr.cdp.downloader.CdpDownloader;
import fr.cdp.downloader.CdpDownloaderEventListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

public class CdpFrame extends JFrame {

    JTextField loginField;
    JPasswordField passwordField;

    CdpOutputLabel outputLabel = new CdpOutputLabel();

    JTextField cdpIdField;

    JTextField dirField;
    JButton launchButton;

    public boolean isDownloading = false;
    public CdpFrame(){
        super("Cahier de prépa Downloader");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel loginLabel = new JLabel("Identifiant: ");
        add(loginLabel, gbc);

        gbc.gridx = 1;

        loginField = new JTextField("");
        loginField.setColumns(15);
        add(loginField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;

        JLabel labelPassword = new JLabel("Mot de passe: ");
        add(labelPassword, gbc);

        gbc.gridx = 1;

        passwordField = new JPasswordField();
        passwordField.setColumns(15);
        add(passwordField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;

        launchButton = new JButton("Télécharger");
        launchButton.addActionListener(e -> {
            if(!isDownloading){
                System.out.println("Downloading");
                download();
            }
            else
                JOptionPane.showMessageDialog(this, "Un téléchargement a déjà lieu",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
        });
        add(launchButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;

        JLabel classLabel = new JLabel("URL CDP: ");
        add(classLabel, gbc);

        gbc.gridx = 1;

        cdpIdField = new JTextField("");
        cdpIdField.setColumns(15);

        add(cdpIdField, gbc);

        gbc.gridx = 2;
        JButton exampleButton = new JButton("Exemples");
        exampleButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "<html>https://cahier-de-prepa.fr/<font color='red'>2004/mp2i-galerie-lafayette</font>/<br>" +
                            "https://cahier-de-prepa.fr/<font color='red'>mpsi-lycee-boulogne</font>/",
                    "Exemples", JOptionPane.PLAIN_MESSAGE);
        });
        add(exampleButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;

        JLabel dirLabel = new JLabel("Répertoire: ");
        add(dirLabel, gbc);

        gbc.gridx = 1;

        dirField = new JTextField(new File("").getAbsolutePath());
        dirField.setColumns(15);
        add(dirField, gbc);

        gbc.gridx = 2;

        JButton dirChoose = new JButton("Choisir répertoire");
        dirChoose.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fileChooser.showOpenDialog(this);
            if(option == JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile();
                dirField.setText(file.getAbsolutePath());
            }else{
                dirField.setText(new File("").getAbsolutePath());
            }
        });

        add(dirChoose, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.gridheight = 10;

        JPanel panel = new JPanel();
        panel.setBackground(Color.gray);
        panel.setBorder(BorderFactory.createBevelBorder(1, Color.BLACK, Color.BLACK));

        outputLabel.updateText();
        panel.add(outputLabel);

        gbc.fill = GridBagConstraints.BOTH;
        add(panel, gbc);

        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Icône");
        JMenuItem item = new JMenuItem("Défaut");
        item.addActionListener(e -> {
            try {
                Image img = ImageIO.read(Objects.requireNonNull(CdpFrame.class.getResourceAsStream("/cdp_icon.png")));
                setIconImage(img);
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        });
        menu.add(item);
        JMenuItem item2 = new JMenuItem("Lalo");
        item2.addActionListener(e -> {
            try {
                Image img = ImageIO.read(Objects.requireNonNull(
                        CdpFrame.class.getResourceAsStream("/cdp_icon_lalo.png")));
                setIconImage(img);
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        });
        menu.add(item2);
        bar.add(menu);
        setJMenuBar(bar);

        pack();
        setResizable(true);
        setVisible(true);

        try {
            Image img = ImageIO.read(Objects.requireNonNull(CdpFrame.class.getResourceAsStream("/cdp_icon.png")));
            setIconImage(img);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void download(){

        String login = loginField.getText();
        char[] password = passwordField.getPassword();
        String urlString = cdpIdField.getText();
        String dir = dirField.getText();

        File directory = new File(dir);
        if(!(directory.exists() && directory.isDirectory())){
            outputLabel.setError("Répertoire inexistant, modifier le répertoire");
            return;
        }

        isDownloading = true;
        launchButton.setBackground(Color.red);
        CdpDownloader downloader = new CdpDownloader(login, String.valueOf(password),
                urlString, CdpDownloaderEventListener.guiEventListener(this));

        Thread thread = new Thread(() -> {
            downloader.download(Paths.get(dir, "cdp_archive").toString());
            launchButton.setBackground(null);
        });

        thread.start();

    }

    public CdpOutputLabel getCdpOutputLabel() {
        return outputLabel;
    }

    public JButton getLaunchButton() {
        return launchButton;
    }
}
