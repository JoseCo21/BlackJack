module di.blackjack {
    requires javafx.controls;
    requires javafx.fxml;


    opens di.blackjack to javafx.fxml;
    exports di.blackjack;
}