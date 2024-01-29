package klijent_aplikacija;

import http.HttpRequestUtil;
import http.HttpResponse;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class FormWindow extends JFrame {

    private Map<String, JTextField> parameterFields;
    private int buttonNumber;
    private MainMenu mainMenu;

    public FormWindow(int buttonNumber, MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        setSize(1200, 800);
        this.buttonNumber = buttonNumber;
        parameterFields = new LinkedHashMap<>();

        switch (buttonNumber) {
            case 1:
                //kreiranje grada
                parameterFields.put("Naziv grada", new JTextField());
                break;
            case 2:
                //kreiranje korisnika
                parameterFields.put("Ime", new JTextField());
                parameterFields.put("Email", new JTextField());
                parameterFields.put("Godiste", new JTextField());
                parameterFields.put("Pol", new JTextField());
                parameterFields.put("Naziv mesta", new JTextField());
                break;
            case 3:
                //promena email-a za korisnika
                parameterFields.put("Email korisnika", new JTextField());
                parameterFields.put("Novi email korisnika", new JTextField());
                break;
            case 4:
                //promena mesta korisnika
                parameterFields.put("Email korisnika", new JTextField());
                parameterFields.put("Naziv novog mesta", new JTextField());
                break;
            case 5:
                //kreiranje kategorije
                parameterFields.put("Naziv kategorije", new JTextField());
                break;
            case 6:
                //kreiranje video snimka
                parameterFields.put("Naziv video snimka", new JTextField());
                parameterFields.put("Trajanje", new JTextField());
                parameterFields.put("Email korisnika", new JTextField());
                break;
            case 7:
                //promena naziva snimka
                parameterFields.put("ID video snimka", new JTextField());
                parameterFields.put("Novi naziv video snimka", new JTextField());
                break;
            case 8:
                //dodavanje kategorije snuimku
                parameterFields.put("ID video snimka", new JTextField());
                parameterFields.put("Naziv kategorije", new JTextField());
                break;
            case 9:
                //kreiranje paketa
                parameterFields.put("Naziv paketa", new JTextField());
                parameterFields.put("Mesecna cena", new JTextField());
                break;
            case 10:
                //promena cene paketa
                parameterFields.put("Naziv paketa", new JTextField());
                parameterFields.put("Nova mesecna cena", new JTextField());
                break;
            case 11:
                //kreiranje pretplate korisnika na paket
                parameterFields.put("Email korisnika", new JTextField());
                parameterFields.put("Naziv paketa", new JTextField());
                break;
            case 12:
                //kreiranje gledanja snimka od strane korisnika
                parameterFields.put("ID video snimka", new JTextField());
                parameterFields.put("Email korisnika", new JTextField());
                parameterFields.put("Sekund snimka pocetka gledanja", new JTextField());
                parameterFields.put("Trajanje gledanja u sekundama", new JTextField());
                break;
            case 13:
                //kreiranje ocene snimka od korisnika
                parameterFields.put("ID video snimka", new JTextField());
                parameterFields.put("Email korisnika", new JTextField());
                parameterFields.put("Ocena", new JTextField());
                break;
            case 14:
                //menjanje ocene snimka od korisnika
                parameterFields.put("ID video snimka", new JTextField());
                parameterFields.put("Email korisnika", new JTextField());
                parameterFields.put("Ocena", new JTextField());
                break;
            case 15:
                //brisanje ocene snimka od korisnika
                parameterFields.put("ID video snimka", new JTextField());
                parameterFields.put("Email korisnika", new JTextField());
                break;
            case 16:
                //brisanje video snimka od strane autora
                parameterFields.put("ID video snimka", new JTextField());
                parameterFields.put("Email korisnika", new JTextField());
                break;
            case 17:
                //dohvatanje svih mesta
                break;
            case 18:
                //dohvatanje svih korisnika
                break;
            case 19:
                //dohvatanje svih kategorija
                break;
            case 20:
                //dohvatanje svih video snimaka
                break;
            case 21:
                //dohvatanje kategorija za odredjeni snimak
                parameterFields.put("ID video snimka", new JTextField());
                break;
            case 22:
                //dohvatanje svih paketa
                break;
            case 23:
                //dohvatanje svih pretplata za korisnika
                parameterFields.put("Email korisnika", new JTextField());
                break;
            case 24:
                //dohvatanje svih gledanja za snimak
                parameterFields.put("ID video snimka", new JTextField());
                break;
            case 25:
                //dohvatanje svih ocena za snimak
                parameterFields.put("ID video snimka", new JTextField());
                break;
            default:
                // Default case
                break;
        }

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        for (Map.Entry<String, JTextField> entry : parameterFields.entrySet()) {
            JLabel label = new JLabel(entry.getKey());
            label.setFont(mainMenu.defaultFont);
            add(label, gbc);
            gbc.gridx++;
            JTextField field = entry.getValue();
            field.setFont(mainMenu.defaultFont);
            field.setPreferredSize(new Dimension(500, (int) field.getPreferredSize().getHeight()));
            add(field, gbc);
            gbc.gridx = 0;
            gbc.gridy++;
        }

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(mainMenu.defaultFont);
        submitButton.addActionListener((ActionEvent e) -> {
            submitParameters();
            dispose();
        });

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = parameterFields.size();
        add(submitButton, gbc);

        setTitle(MainMenu.getButtonActionName(buttonNumber, false));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void submitParameters() {
        Map<String, String> parameters = new HashMap<>();
        for (Map.Entry<String, JTextField> entry : parameterFields.entrySet()) {
            parameters.put(entry.getKey(), entry.getValue().getText());
        }

        HttpResponse actionResponse = HttpRequestUtil.sendRequest(buttonNumber, parameters);
        mainMenu.setOutputText(actionResponse.getResponseBody());
        System.out.println(actionResponse.getResponseBody());
    }

    public void clickSubmitButton() {
        JButton submitButton = findSubmitButton();
        if (submitButton != null) {
            submitButton.doClick();
        }
    }

    private JButton findSubmitButton() {
        for (Component component : getContentPane().getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if ("Submit".equals(button.getText())) {
                    return button;
                }
            }
        }
        return null;
    }
}
