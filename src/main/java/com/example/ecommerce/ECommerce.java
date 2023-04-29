package com.example.ecommerce;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import static com.example.ecommerce.Product.getAllProducts;

public class ECommerce extends Application {

    private final int width=700,height =600,headerLine=50;
    ProductList productList = new ProductList();

    Pane bodyPane;

    ObservableList<Product> cartItemList = FXCollections.observableArrayList();

    Button signButton = new Button("Sign Out");

    Button placeOrderButton = new Button("Place Order");

    Label welcomeLabel = new Label("Welcome Customer");

    String searchName = new String();


    Customer loggedInCustomer = null;


    Order order = new Order();

    private void addItemsToCart(Product product){
        if(cartItemList.contains(product))
            return;
        cartItemList.add(product);
        System.out.println("Products in Cart" + cartItemList.stream().count());
    }
    private GridPane headerBar(){
        GridPane header = new GridPane();

        TextField searchBar = new TextField();
        Button searchButton = new Button("search");
        Button cartButton = new Button("Cart");

        Button ordersButton = new Button("Orders");

        ordersButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
//                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(order.getOrders());
            }
        });


        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                bodyPane.getChildren().add(productList.getAllSearchedProducts(searchName));
            }
        });

        signButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
              bodyPane.getChildren().clear();
              bodyPane.getChildren().add(loginPage());
            }
        });

        cartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
//                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(productList.productsInCart(cartItemList));
            }
        });


        bodyPane.getChildren().add(productList.getAllProducts());
        bodyPane.setTranslateY(0);

        header.setHgap(10);
        header.add(searchBar,0,0);
        header.add(searchButton,1,0);
        header.add(welcomeLabel,10,0);
        header.add(cartButton,19,0);
        header.add(ordersButton,20,0);
        header.add(signButton,21,0);
        return header;
    }

    private GridPane loginPage(){
        Label userLabel = new Label("User Name");
        Label passLabel = new Label("Password");
        TextField userName = new TextField();
        userName.setPromptText("Enter User Name");
        PasswordField password = new PasswordField();
        password.setPromptText("Enter Password");
        Button loginButton = new Button("LogIn");
        Label messageLabel = new Label("LogIn Message");

        // working of login button using actionlistener
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
               String user = userName.getText();
               String pass = password.getText();
                try {
                    loggedInCustomer = Login.customerLogin(user,pass);
                    if(loggedInCustomer != null){
                        messageLabel.setText("LogIn Successfull!!");
                        messageLabel.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
                        welcomeLabel.setText("Welcome " + loggedInCustomer.getName());
                        welcomeLabel.setStyle("-fx-text-fill: blue; -fx-font-size: 16px;");
                        bodyPane.getChildren().clear();
                          bodyPane.getChildren().add(headerBar());
                          bodyPane.getChildren().add(footerBar());



                    }
                    else {
                        messageLabel.setText("LogIn Failed!!");
                        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        GridPane loginPane = new GridPane();

       loginPane.setTranslateX(190);
       loginPane.setTranslateY(50);
        loginPane.setVgap(15);
        loginPane.setHgap(15);
        loginPane.setStyle("-fx-font: 15 arial;");
        loginPane.add(userLabel,0,0);
        loginPane.add(userName,1,0);
        loginPane.add(passLabel,0,1);
        loginPane.add(password,1,1);
        loginPane.add(loginButton,0,2);
        loginPane.add(messageLabel,1,2);


        return loginPane;

    }

    private void showDialogue(String message){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Order Status");
        ButtonType type = new ButtonType("Ok",ButtonBar.ButtonData.OK_DONE);
        dialog.setContentText(message);
        dialog.getDialogPane().getButtonTypes().add(type);

        dialog.showAndWait();
    }

    private GridPane footerBar(){
        Button buyNowButton = new Button("Buy Now");
        Button addToCart = new Button("Add To Cart");


        buyNowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product = productList.getSelectedProduct();
                boolean orderStatus = false;
                if(product!=null && loggedInCustomer!=null){
                    orderStatus = order.placeOrder(loggedInCustomer,product);
                }
                if(orderStatus==true){
                    //
                    showDialogue("Order Successful!!");
                }
                else {
                    //
                }
            }
        });

        addToCart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product = productList.getSelectedProduct();
                addItemsToCart(product);
            }
        });

        placeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int orderCount=0;
                if(!cartItemList.isEmpty() && loggedInCustomer!=null){
                    orderCount = order.placeOrderMultipleProducts(cartItemList,loggedInCustomer);
                }
                if(orderCount > 0){
                    //
                    showDialogue("Order for " + orderCount + "products placed Successfully!!");
                }
                else {
                    //
                }
            }
        });

        GridPane footer = new GridPane();
        footer.setTranslateY(headerLine+ height);
        footer.setTranslateX(50);
        footer.add(buyNowButton,0,0);
        footer.add(addToCart,1,0);
        footer.setHgap(10);
        footer.add(placeOrderButton,2,0);
        return footer;
    }
    private Pane createContent(){
        Pane root = new Pane();
        root.setPrefSize(width,height+2*headerLine);

        bodyPane = new Pane();
        bodyPane.setTranslateY(headerLine);
        bodyPane.setTranslateX(10);

        bodyPane.getChildren().add(loginPage());
        bodyPane.setPrefSize(width,height);
        root.getChildren().addAll(
//                loginPage(),
//                productList.getAllProducts(),
                bodyPane

        );
        return root;
    }
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(ECommerce.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(createContent());
        stage.setTitle("ECommerce");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {launch();}
}