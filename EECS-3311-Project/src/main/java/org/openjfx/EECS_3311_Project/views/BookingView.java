package org.openjfx.EECS_3311_Project.views;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import org.openjfx.EECS_3311_Project.model.Booking;

import java.time.format.DateTimeFormatter;

public class BookingView extends ListCell<Booking>
{
    private final VBox card;
    private final Label bookingNameLabel;
    private final Label dateLabel;
    private final Label roomLabel;
    private final Label timeLabel;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mm a");

    public BookingView()
    {
        //initializing the labels
        bookingNameLabel = new Label();
        bookingNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");

        dateLabel = new Label();
        dateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");

        roomLabel = new Label();
        roomLabel.setStyle("-fx-text-fill: #333333; -fx-font-size: 12px;");

        timeLabel = new Label();
        timeLabel.setStyle("-fx-text-fill: #333333; -fx-font-size: 12px;");

        //initializing the card
        card = new VBox(5, bookingNameLabel, dateLabel, roomLabel, timeLabel);

        //align text
        card.setAlignment(Pos.CENTER);

        //styling the card
        card.setStyle(
                "-fx-border-color: black;" +
                        "-fx-border-width: 1;" +
                        "-fx-background-color: white;"
        );

        card.setPadding(new javafx.geometry.Insets(10));

    }

    @Override
    protected void updateItem(Booking newBooking, boolean isEmpty)
    {
        super.updateItem(newBooking, isEmpty);

        if (isEmpty || newBooking == null)
        {
            setText(null);
            setGraphic(null);

            setStyle("-fx-background-color: transparent;");
        }
        else {
            bookingNameLabel.setText(newBooking.getName());
            dateLabel.setText(newBooking.getStartTime().getMonth().name() + " " + newBooking.getStartTime().getDayOfMonth() + ", " + newBooking.getStartTime().getYear());
            roomLabel.setText(newBooking.getRoomId());
            timeLabel.setText(newBooking.getStartTime().format(TIME_FORMATTER) + " - " + newBooking.getEndTime().format(TIME_FORMATTER));

            setGraphic(card);

            setStyle("-fx-background-color: transparent; -fx-padding: 5px 10px 5px 10px;");

        }
    }

}
