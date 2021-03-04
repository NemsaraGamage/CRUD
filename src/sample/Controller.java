package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.Initializable;
import java.awt.print.Book;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Controller {
    @FXML
    private TextField tfid;

    @FXML
    private TextField tftitle;

    @FXML
    private TextField tfauthor;

    @FXML
    private TextField tfyear;

    @FXML
    private TextField tfpages;

    @FXML
    private TableView<Books> tvBooks;

    @FXML
    private TableColumn<Books, Integer> colId;

    @FXML
    private TableColumn<Books, String> colTitle;

    @FXML
    private TableColumn<Books, String> colAuthor;

    @FXML
    private TableColumn<Books, Integer> colYear;

    @FXML
    private TableColumn<Books, Integer> colPages;

    @FXML
    private Button buttonInsert;

    @FXML
    private Button buttonUpdate;

    @FXML
    private Button buttonDelete;

    @FXML
    private void ButtonAction(ActionEvent event){
        if (event.getSource() == buttonInsert){
            insertRecord();
        }else if (event.getSource() == buttonUpdate){
            updateRecord();
        }else if(event.getSource() == buttonDelete){
            deleteButton();
        }
    }

    public void initialize(URL url, ResourceBundle rb) {

        showBooks();
    }

    public Connection getConnection(){
        Connection conn;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root","");
            return conn;
        }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
    }

    public ObservableList<Books> getBookList(){
        ObservableList<Books> booklist = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "Select * FROM books";
        Statement st;
        ResultSet rs;
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Books books;
            while(rs.next()){
                books = new Books(rs.getInt("id"), rs.getString("title"),rs.getString("author"),rs.getInt("year"),rs.getInt("pages"));
                booklist.add(books);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return booklist;

    }
    public void showBooks(){
        ObservableList<Books> list = getBookList();
        colId.setCellValueFactory(new PropertyValueFactory<Books, Integer>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<Books, String>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<Books, String>("author"));
        colYear.setCellValueFactory(new PropertyValueFactory<Books, Integer>("year"));
        colPages.setCellValueFactory(new PropertyValueFactory<Books, Integer>("page"));

        tvBooks.setItems(list);

    }
    private void insertRecord(){
        String query = "INSERT INTO books Value (" + tfid.getText() + ",'" + tftitle.getText()+"','"+tfauthor.getText() + "'," + tfyear.getText() +","+ tfpages.getText()+")";
        executeQuery(query);
        showBooks();
    }
    private void updateRecord(){
        String query = "UPDATE  books SET title  = '" + tftitle.getText() + "', author = '" + tfauthor.getText() + "', year = " +
                tfyear.getText() + ", pages = " + tfpages.getText() + " WHERE id = " + tfid.getText() + "";
        executeQuery(query);
        showBooks();
    }
    private void deleteButton(){
        String query = "DELETE FROM books WHERE id =" + tfid.getText() + "";
        executeQuery(query);
        showBooks();
    }
    private void executeQuery(String query){
        Connection conn = getConnection();
        Statement st;
        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
