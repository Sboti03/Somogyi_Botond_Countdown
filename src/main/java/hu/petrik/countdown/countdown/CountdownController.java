package hu.petrik.countdown.countdown;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class CountdownController {

    @FXML
    private TextField inputField;
    @FXML
    private Button startBtn;
    @FXML
    private Label outLabel;

    @FXML
    public void start(ActionEvent actionEvent) {
        LocalDateTime goalTime;
        try {
            goalTime = parseToLocalDateTime(inputField.getText());
        } catch (Exception e) {
            makeAlert("Hibás formátum");
            return;
        }

        if (goalTime.isBefore(LocalDateTime.now())) {
            makeAlert("Hibás dátum!");
            return;
        }
        startBtn.setDisable(true);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()-> {
                    Duration duration = Duration.between(LocalTime.now(),goalTime.toLocalTime());
                    Period period = Period.between(LocalDate.now(), goalTime.toLocalDate());
                    if(duration.isNegative()) {
                        duration = Duration.between(LocalTime.now(), LocalTime.parse("23:59:59"));
                        period = period.minusDays(1);
                    }
                    if (goalTime.isBefore(LocalDateTime.now())) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Vége");
                        alert.setContentText("Elérte a beállított időzétést");
                        alert.show();
                        outLabel.setText("");
                        startBtn.setDisable(false);
                        timer.cancel();
                    }

                    long seconds = duration.getSeconds();
                    long HH = seconds / 3600;
                    long MM = (seconds % 3600) / 60;
                    long SS = (seconds % 60);
                    System.out.printf("%sév %shó %snap %s:%s:%s%n\n",
                            period.getYears(), period.getMonths(), period.getDays(), HH,MM,SS);
                    outLabel.setText(String.format("%sév %shó %snap %s:%s:%s",
                    period.getYears(), period.getMonths(), period.getDays(), HH,MM,SS));
                });
            }
        };
        timer.schedule(timerTask, 0,1000);

    }
    private LocalDateTime parseToLocalDateTime(String value) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm:ss");
        return LocalDateTime.parse(value, dtf);
    }

    private void makeAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Hibás adat!");
        alert.setContentText(msg);
        alert.show();
    }
}