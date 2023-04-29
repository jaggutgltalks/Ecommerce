package com.example.ecommerce;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class ProductList {

    public TableView<Product> productTable;

    public Pane getAllProducts(){

        ObservableList<Product> productList = Product.getAllProducts();
        return createTableFromList(productList);

//        ObservableList<Product> data = FXCollections.observableArrayList();
//        data.addAll(new Product(123, "HP Laptop",72001.6),
//                new Product(1245,"ASUS Laptop", 28010.5));


    }

    public Pane createTableFromList(ObservableList<Product> productList){
        TableColumn id = new TableColumn("Id");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn name = new TableColumn("name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn price = new TableColumn("price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));


        productTable = new TableView<>();
        productTable.setItems(productList);
        productTable.getColumns().addAll(id,name,price);


        Pane tablePane = new Pane();
        tablePane.getChildren().add(productTable);
        tablePane.setTranslateY(40);
        tablePane.setTranslateX(15);


        return tablePane;
    }

    public Pane productsInCart(ObservableList<Product> productList){
        return createTableFromList(productList);
    }

    //search functionality implementaion
    public Pane getAllSearchedProducts(String searchName){
        TableColumn id = new TableColumn("Item");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn name = new TableColumn("Mobile");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn price = new TableColumn("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

//        TableColumn quantity = new TableColumn("Quantity");
//        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // select * from products where name like 'a%';
        String query = "SELECT * FROM products WHERE name LIKE '%" + searchName + "%'";
        ObservableList<Product> productsList = Product.getProducts(query);

        productTable = new TableView<>();
        productTable.setItems(productsList);
        productTable.getColumns().addAll(id, name, price);

        Pane tablePane = new Pane();
        tablePane.getChildren().add(productTable);
        tablePane.setTranslateY(40);
        tablePane.setTranslateX(15);

        return tablePane;
    }

    public Product getSelectedProduct(){
        return productTable.getSelectionModel().getSelectedItem();
    }
}
