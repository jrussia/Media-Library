package media;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.sql.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;





/*
 * CSC 478
 * Team2
 * mediaLibrary.java
 * Purpose: To store a library of movies, books, and cds 
 * with the functionality to add, search, modify, and delete.
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 1.1.4 3/6/15
 */

public class mediaLibrary extends JFrame{
	public String databaseFilePath = "C:/Users/User/workspace/mediaLibrary/database.db";
	
	public Connection connection(String filePath){
		String fullPath = "jdbc:sqlite:" + filePath;
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		final Connection conn;
		final Statement stat;
		try {
			conn = DriverManager.getConnection(fullPath);
			stat = conn.createStatement();
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
	}
		return null;
	}
	
	public Media [] search(){
		//switch to the above line when moving to DBController
		//Connection conn = mediaLibrary.connection(databaseFilePath);
		Connection conn = connection(databaseFilePath);
		try {
			String sql = "SELECT ";
			Statement stat = conn.createStatement();
			ResultSet match = stat.executeQuery(sql);
			//TODO make an object for every match and add them to the table
			while(match.next()){
				
			}
		}
	catch (SQLException e) {
		e.printStackTrace();
	}
		return null;
	}
	
	public mediaLibrary(){
		//********************Populate ComboBox options with current database entries***********************
		LinkedList countryList = new LinkedList();
		LinkedList languageList = new LinkedList();
		LinkedList genreMovieList = new LinkedList();
		LinkedList genreBookList = new LinkedList();
		LinkedList genreCDList = new LinkedList();
		LinkedList authorMovieList = new LinkedList();
		LinkedList authorBookList = new LinkedList();
		LinkedList authorCDList = new LinkedList();
		Connection conn = connection(databaseFilePath);
			try {
				Statement stat = conn.createStatement();
				ResultSet rsCountry = stat.executeQuery("select * from country");
				while(rsCountry.next()){
					countryList.add(rsCountry.getString("country"));
				}
				ResultSet rsLanguage = stat.executeQuery("select * from language");
				while(rsLanguage.next()){
					languageList.add(rsLanguage.getString("language"));
				}
				ResultSet rsMovieGenre = stat.executeQuery("select * from genre where media_type = 'movie'");
				while(rsMovieGenre.next()){
					genreMovieList.add(rsMovieGenre.getString("genre"));
				}
				ResultSet rsBookGenre = stat.executeQuery("select * from genre where media_type = 'book'");
				while(rsBookGenre.next()){
					genreBookList.add(rsBookGenre.getString("genre"));
				}
				ResultSet rsCDGenre = stat.executeQuery("select * from genre where media_type = 'cd'");
				while(rsCDGenre.next()){
					genreCDList.add(rsCDGenre.getString("genre"));
				}
				ResultSet rsMovieAuthor = stat.executeQuery("select * from author where media_type = 'movie'");
				while(rsMovieAuthor.next()){
					authorMovieList.add(rsMovieAuthor.getString("author"));
				}
				ResultSet rsBookAuthor = stat.executeQuery("select * from author where media_type = 'book'");
				while(rsBookAuthor.next()){
					authorBookList.add(rsBookAuthor.getString("author"));
				}
				ResultSet rsCDAuthor = stat.executeQuery("select * from author where media_type = 'cd'");
				while(rsCDAuthor.next()){
					authorCDList.add(rsCDAuthor.getString("author"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
		}
			String [] countriesArray = new String[countryList.size()];
			String [] languagesArray = new String[languageList.size()];
			String [] genreMovieArray = new String[genreMovieList.size()];
			String [] genreBookArray = new String[genreBookList.size()];
			String [] genreCDArray = new String[genreCDList.size()];
			String [] authorMovieArray = new String[authorMovieList.size()];
			String [] authorBookArray = new String[authorBookList.size()];
			String [] authorCDArray = new String[authorCDList.size()];
			for(int i = 0; i < countryList.size(); i++){
				countriesArray[i] = (String) countryList.get(i);
			}
			for(int i = 0; i < languageList.size(); i++){
				languagesArray[i] = (String) languageList.get(i);
			}
			for(int i = 0; i < genreMovieList.size(); i++){
				genreMovieArray[i] = (String) genreMovieList.get(i);
			}
			for(int i = 0; i < genreBookList.size(); i++){
				genreBookArray[i] = (String) genreBookList.get(i);
			}
			for(int i = 0; i < genreCDList.size(); i++){
				genreCDArray[i] = (String) genreCDList.get(i);
			}
			for(int i = 0; i < authorMovieList.size(); i++){
				authorMovieArray[i] = (String) authorMovieList.get(i);
			}
			for(int i = 0; i < authorBookList.size(); i++){
				authorBookArray[i] = (String) authorBookList.get(i);
			}
			for(int i = 0; i < authorCDList.size(); i++){
				authorCDArray[i] = (String) authorCDList.get(i);
			}
		//**********************************GUI creation************************************		
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		/*Panel for option to choose between movie, book, cd*/
		JPanel addOptionPanel = new JPanel();
		addOptionPanel.setLayout(new GridLayout(3,1,10,10));
		final JRadioButton movieOptionBtn = new JRadioButton("Movie");
		movieOptionBtn.setActionCommand("Movie");
		final JRadioButton bookOptionBtn = new JRadioButton("Book");
		bookOptionBtn.setActionCommand("Book");
		final JRadioButton cdOptionBtn = new JRadioButton("CD");
		cdOptionBtn.setActionCommand("CD");
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(movieOptionBtn);
		buttonGroup.add(bookOptionBtn);
		buttonGroup.add(cdOptionBtn);
		addOptionPanel.add(movieOptionBtn);
		addOptionPanel.add(bookOptionBtn);
		addOptionPanel.add(cdOptionBtn);
		
		/*text fields for adding movie content*/
		final JPanel addMovieEntriesPanel = new JPanel();
		addMovieEntriesPanel.setLayout(new FlowLayout());
		final JPanel addMovieEntriesPanel2 = new JPanel();
		addMovieEntriesPanel2.setLayout(new GridLayout(13, 3, 10, 10));
		final JLabel movieCoverUploadStatusLbl = new JLabel();
		JLabel movieISBNLbl = new JLabel("ISBN");
		final JTextField movieISBNTxt = new JTextField();
		JLabel movieIDBlank = new JLabel();
		JLabel movieTitleLbl = new JLabel("Title");
		final JTextField movieTitleTxt = new JTextField();
		JLabel movieTitleBlank = new JLabel();
		JLabel movieDirectorLbl = new JLabel("Director");
		final JComboBox movieDirectorCombo = new JComboBox(authorMovieArray);
		JButton movieDirectorBtn = new JButton("Add Director");
		JLabel movieGenreLbl = new JLabel("Genre");
		final JComboBox movieGenreCombo = new JComboBox(genreMovieArray);
		JButton movieGenreNewBtn = new JButton("Add Genre");
		JLabel movieYearLbl = new JLabel("Year");
		final JTextField movieYearTxt = new JTextField();
		JLabel movieYearBlank = new JLabel();
		JLabel movieLengthLbl = new JLabel("Length (minutes)");
		final JTextField movieLengthTxt = new JTextField();
		JLabel movieLengthBlank = new JLabel();
		JLabel movieLanguageLbl = new JLabel("Language");
		final JComboBox movieLanguageCombo = new JComboBox(languagesArray);
		JButton movieLanguageNewBtn = new JButton("Add Language");
		JLabel movieCountryLbl = new JLabel("Country of Origin");
		final JComboBox movieCountryCombo = new JComboBox(countriesArray);
		JButton movieCountryNewBtn = new JButton("Add Country");
		JLabel movieCastLbl = new JLabel("Top Cast");
		final JTextField movieCastTxt = new JTextField();
		JLabel movieCastBlank = new JLabel();
		JLabel moviePlotLbl = new JLabel("Plot Summary");
		final JTextField moviePlotTxt = new JTextField();
		JLabel moviePlotBlank = new JLabel();
		JLabel placeHolder1 = new JLabel();
		JButton addMovieBtn = new JButton("Add");
		final JLabel addMovieStatusLbl = new JLabel();
		addMovieEntriesPanel2.add(movieISBNLbl);
		addMovieEntriesPanel2.add(movieISBNTxt);
		addMovieEntriesPanel2.add(movieIDBlank);
		addMovieEntriesPanel2.add(movieTitleLbl);
		addMovieEntriesPanel2.add(movieTitleTxt);
		addMovieEntriesPanel2.add(movieTitleBlank);
		addMovieEntriesPanel2.add(movieDirectorLbl);
		addMovieEntriesPanel2.add(movieDirectorCombo);
		addMovieEntriesPanel2.add(movieDirectorBtn);
		addMovieEntriesPanel2.add(movieGenreLbl);
		addMovieEntriesPanel2.add(movieGenreCombo);
		addMovieEntriesPanel2.add(movieGenreNewBtn);
		addMovieEntriesPanel2.add(movieYearLbl);
		addMovieEntriesPanel2.add(movieYearTxt);
		addMovieEntriesPanel2.add(movieYearBlank);
		addMovieEntriesPanel2.add(movieLengthLbl);
		addMovieEntriesPanel2.add(movieLengthTxt);
		addMovieEntriesPanel2.add(movieLengthBlank);
		addMovieEntriesPanel2.add(movieLanguageLbl);
		addMovieEntriesPanel2.add(movieLanguageCombo);
		addMovieEntriesPanel2.add(movieLanguageNewBtn);
		addMovieEntriesPanel2.add(movieCountryLbl);
		addMovieEntriesPanel2.add(movieCountryCombo);
		addMovieEntriesPanel2.add(movieCountryNewBtn);
		addMovieEntriesPanel2.add(movieCastLbl);
		addMovieEntriesPanel2.add(movieCastTxt);
		addMovieEntriesPanel2.add(movieCastBlank);
		addMovieEntriesPanel2.add(moviePlotLbl);
		addMovieEntriesPanel2.add(moviePlotTxt);
		addMovieEntriesPanel2.add(moviePlotBlank);
		addMovieEntriesPanel2.add(placeHolder1);
		addMovieEntriesPanel2.add(addMovieBtn);
		addMovieEntriesPanel2.add(addMovieStatusLbl);
		addMovieEntriesPanel.add(movieCoverUploadStatusLbl);
		addMovieEntriesPanel.add(addMovieEntriesPanel2);
		
		/*text fields for adding book content*/
		final JPanel addBookEntriesPanel2 = new JPanel();
		addBookEntriesPanel2.setLayout(new GridLayout(8,3,10,10));
		final JPanel addBookEntriesPanel = new JPanel();
		addBookEntriesPanel.setLayout(new FlowLayout());
		final JLabel bookCoverUploadStatusLbl = new JLabel();
		JLabel bookISBNLbl = new JLabel("ISBN");
		final JTextField bookISBNTxt = new JTextField();
		JLabel bookISBNBlank = new JLabel();
		JLabel bookTitleLbl = new JLabel("Title");
		final JTextField bookTitleTxt = new JTextField();
		JLabel bookTitleBlank = new JLabel();
		JLabel bookAuthorLbl = new JLabel("Author");
		final JComboBox bookAuthorCombo = new JComboBox(authorBookArray);
		final JButton bookAuthorBtn = new JButton("Add Author");
		JLabel bookLengthLbl = new JLabel("Length (pages)");
		final JTextField bookLengthTxt = new JTextField();
		JLabel bookLengthBlank = new JLabel();
		JLabel bookYearLbl = new JLabel("Year Published");
		final JTextField bookYearTxt = new JTextField();
		JLabel bookYearBlank = new JLabel();
		JLabel bookGenreLbl = new JLabel("Genre");
		final JComboBox bookGenreCombo = new JComboBox(genreBookArray);
		final JButton bookGenreBtn = new JButton("Add Genre");
		JLabel bookPlotLbl = new JLabel("Plot Summary");
		final JTextField bookPlotTxt = new JTextField();
		JLabel bookPlotBlank = new JLabel();
		JPanel placeHolder4 = new JPanel();
		JButton addBookBtn = new JButton("Add");
		final JLabel addBookStatusLbl = new JLabel();
		addBookEntriesPanel2.add(bookISBNLbl);
		addBookEntriesPanel2.add(bookISBNTxt);
		addBookEntriesPanel2.add(bookISBNBlank);
		addBookEntriesPanel2.add(bookTitleLbl);
		addBookEntriesPanel2.add(bookTitleTxt);
		addBookEntriesPanel2.add(bookTitleBlank);
		addBookEntriesPanel2.add(bookAuthorLbl);
		addBookEntriesPanel2.add(bookAuthorCombo);
		addBookEntriesPanel2.add(bookAuthorBtn);
		addBookEntriesPanel2.add(bookLengthLbl);
		addBookEntriesPanel2.add(bookLengthTxt);
		addBookEntriesPanel2.add(bookLengthBlank);
		addBookEntriesPanel2.add(bookYearLbl);
		addBookEntriesPanel2.add(bookYearTxt);
		addBookEntriesPanel2.add(bookYearBlank);
		addBookEntriesPanel2.add(bookGenreLbl);
		addBookEntriesPanel2.add(bookGenreCombo);
		addBookEntriesPanel2.add(bookGenreBtn);
		addBookEntriesPanel2.add(bookPlotLbl);
		addBookEntriesPanel2.add(bookPlotTxt);
		addBookEntriesPanel2.add(bookPlotBlank);
		addBookEntriesPanel2.add(placeHolder4);
		addBookEntriesPanel2.add(addBookBtn);
		addBookEntriesPanel2.add(addBookStatusLbl);
		addBookEntriesPanel.add(bookCoverUploadStatusLbl);
		addBookEntriesPanel.add(addBookEntriesPanel2);
		
		/*text fields for adding CD content*/
		final JPanel addCDEntriesPanel2 = new JPanel();
		addCDEntriesPanel2.setLayout(new GridLayout(7,3,10,10));
		final JPanel addCDEntriesPanel = new JPanel();
		addCDEntriesPanel.setLayout(new FlowLayout());
		final JLabel CDCoverUploadStatusLbl = new JLabel();
		JLabel CDISBNLbl = new JLabel("ISBN");
		final JTextField CDISBNTxt = new JTextField();
		JLabel CDISBNBlank = new JLabel();
		JLabel CDArtistLbl = new JLabel("Artist/Band");
		final JComboBox CDArtistCombo = new JComboBox(authorCDArray);
		final JButton CDArtistBtn = new JButton("Add Author");
		JLabel CDAlbumLbl = new JLabel("Album Title");
		final JTextField CDAlbumTxt = new JTextField();
		JLabel CDAlbumBlank = new JLabel();
		JLabel CDGenreLbl = new JLabel("Genre");
		final JComboBox CDGenreCombo = new JComboBox(genreCDArray);
		JButton CDGenreNewBtn = new JButton("Add Genre");
		JLabel placeHolder6 = new JLabel();
		JButton addCDBtn = new JButton("Add");
		final JLabel addCDStatusLbl = new JLabel();
		addCDEntriesPanel2.add(CDISBNLbl);
		addCDEntriesPanel2.add(CDISBNTxt);
		addCDEntriesPanel2.add(CDISBNBlank);
		addCDEntriesPanel2.add(CDArtistLbl);
		addCDEntriesPanel2.add(CDArtistCombo);
		addCDEntriesPanel2.add(CDArtistBtn);
		addCDEntriesPanel2.add(CDAlbumLbl);
		addCDEntriesPanel2.add(CDAlbumTxt);
		addCDEntriesPanel2.add(CDAlbumBlank);
		addCDEntriesPanel2.add(CDGenreLbl);
		addCDEntriesPanel2.add(CDGenreCombo);
		addCDEntriesPanel2.add(CDGenreNewBtn);
		addCDEntriesPanel2.add(placeHolder6);
		addCDEntriesPanel2.add(addCDBtn);
		addCDEntriesPanel2.add(addCDStatusLbl);
		addCDEntriesPanel.add(CDCoverUploadStatusLbl);
		addCDEntriesPanel.add(addCDEntriesPanel2);
		
		/*Panel for buttons to add automatically, and import cover*/
		JPanel addButtonsPanel = new JPanel();
		addButtonsPanel.setLayout(new GridLayout(3,1,10,10));
		JButton addByISBNBtn = new JButton("Automatically add by ISBN");
		JButton addByImageRecBtn = new JButton("Automatically add by image recognition of the cover");
		JButton importCoverBtn = new JButton("Import Cover Image");
		addButtonsPanel.add(addByISBNBtn);
		addButtonsPanel.add(addByImageRecBtn);
		addButtonsPanel.add(importCoverBtn);
		
		/*popup panel to choose how to bring an image in*/
		final JPanel imagePopupPanel = new JPanel();
		imagePopupPanel.setLayout(new FlowLayout());
		final JButton importImageBtn = new JButton("Import Image");
		JButton takePhotoBtn = new JButton("Take Photo");
		imagePopupPanel.add(importImageBtn);
		imagePopupPanel.add(takePhotoBtn);
		
		/*front tab panel, the panel that shows depends on which media type is selected*/
		final JPanel addCenterPanel = new JPanel();
		addCenterPanel.setLayout(new BoxLayout(addCenterPanel, BoxLayout.Y_AXIS));
		addCenterPanel.add(addCDEntriesPanel);
		addCenterPanel.add(addBookEntriesPanel);
		addCenterPanel.add(addMovieEntriesPanel);
		addCDEntriesPanel.setVisible(false);
		addBookEntriesPanel.setVisible(false);
		addMovieEntriesPanel.setVisible(false);
		
		/*all widgets for the add Tab*/
		final JPanel addPanel = new JPanel();
		addPanel.setLayout(new BorderLayout());
		addPanel.add(addOptionPanel, BorderLayout.WEST);
		addPanel.add(addButtonsPanel, BorderLayout.NORTH);
		addPanel.add(addCenterPanel, BorderLayout.CENTER);
		
		/*the top widgets on the lookup tab*/
		JPanel topSearchPanel = new JPanel();
		topSearchPanel.setLayout(new GridLayout(1,5,10,10));
		JLabel searchLbl = new JLabel("Search");
		JTextField searchTxt = new JTextField();
		JButton searchEnterBtn = new JButton("Enter");
		JButton searchByPhotoBtn = new JButton("Search By Photo");
		JButton searchDisplayAllBtn = new JButton("Display All");
		topSearchPanel.add(searchLbl);
		topSearchPanel.add(searchTxt);
		topSearchPanel.add(searchEnterBtn);
		topSearchPanel.add(searchByPhotoBtn);
		topSearchPanel.add(searchDisplayAllBtn);
		
		/*tables in the lookup tab*/
		JPanel searchTablesPanel = new JPanel();
		searchTablesPanel.setLayout(new BoxLayout(searchTablesPanel, BoxLayout.Y_AXIS));
		JLabel tablesMoviesLbl = new JLabel("Movies");
		String[] movieColumnNames = {"Cover", "Barcode", "Title", "Director", "Genre", "Year", "Length", "Language", "Country", "Cast", "Plot"};
		Object[][] movieTableData = {};
		JTable movieTable = new JTable(movieTableData, movieColumnNames);
		JScrollPane movieTableScrollPane = new JScrollPane(movieTable);
		movieTableScrollPane.setPreferredSize(getPreferredSize());
		JLabel tablesBooksLbl = new JLabel("Books");
		String[] bookColumnNames = {"Cover", "ISBN", "Title", "Author", "Genre", "Year Published", "Length", "Plot"};
		Object[][] bookTableData = {};
		JTable bookTable = new JTable(bookTableData, bookColumnNames);
		JScrollPane bookTableScrollPane = new JScrollPane(bookTable);
		bookTableScrollPane.setPreferredSize(getPreferredSize());
		JLabel tablesCDLbl = new JLabel("CDs");
		String[] CDColumnNames = {"Cover", "Barcode", "Artist/Band", "Album Title", "Genre"};
		Object[][] CDTableData = {};
		JTable CDTable = new JTable(CDTableData, CDColumnNames);
		JScrollPane CDTableScrollPane = new JScrollPane(CDTable);
		CDTableScrollPane.setPreferredSize(getPreferredSize());
		searchTablesPanel.add(tablesMoviesLbl);
		searchTablesPanel.add(movieTableScrollPane);
		searchTablesPanel.add(tablesBooksLbl);
		searchTablesPanel.add(bookTableScrollPane);
		searchTablesPanel.add(tablesCDLbl);
		searchTablesPanel.add(CDTableScrollPane);
		
		/*all widgets for the lookup Tab*/
		final JPanel lookupPanel = new JPanel();
		lookupPanel.setLayout(new BorderLayout());
		lookupPanel.add(topSearchPanel, BorderLayout.NORTH);
		lookupPanel.add(searchTablesPanel, BorderLayout.CENTER);
		
		/*top widgets on the manage tab*/
		JPanel manageTopPanel = new JPanel();
		manageTopPanel.setLayout(new GridLayout(1,3,10,10));
		JLabel manageSearchByISBNLbl = new JLabel("Search by ISBN");
		JTextField manageSearchByISBNTxt = new JTextField();
		JButton manageEnterBtn = new JButton("Enter");
		manageTopPanel.add(manageSearchByISBNLbl);
		manageTopPanel.add(manageSearchByISBNTxt);
		manageTopPanel.add(manageEnterBtn);
		
		/*all widgets for the manage Tab*/
		final JPanel managePanel = new JPanel();
		managePanel.setLayout(new FlowLayout());
		JButton manageSearchByBarcodeBtn = new JButton("Search by Barcode");
		JButton manageSearchByCoverPhotoBtn = new JButton("Search by Cover Photo");
		managePanel.add(manageTopPanel);
		managePanel.add(manageSearchByBarcodeBtn);
		managePanel.add(manageSearchByCoverPhotoBtn);
		
		/*text fields for editing or deleting movie content on manage page*/
		final JPanel manageMovieEntriesPanel = new JPanel();
		manageMovieEntriesPanel.setLayout(new GridLayout(13, 3, 10, 10));
		JLabel manageMovieCoverUploadStatusLbl = new JLabel();
		JLabel managePlaceHolder2 = new JLabel();
		JLabel managePlaceholder = new JLabel();
		JLabel manageMovieISBNLbl = new JLabel("ISBN");
		final JTextField manageMovieISBNTxt = new JTextField();
		JLabel manageMovieIDBlank = new JLabel();
		JLabel manageMovieTitleLbl = new JLabel("Title");
		final JTextField manageMovieTitleTxt = new JTextField();
		JLabel manageMovieTitleBlank = new JLabel();
		JLabel manageMovieDirectorLbl = new JLabel("Director");
		final JComboBox manageMovieDirectorCombo = new JComboBox(authorMovieArray);
		JButton manageMovieDirectorBtn = new JButton("Add Director");
		JLabel manageMovieGenreLbl = new JLabel("Genre");
		final JComboBox manageMovieGenreCombo = new JComboBox(genreMovieArray);
		JButton manageMovieGenreNewBtn = new JButton("Add Genre");
		JLabel manageMovieYearLbl = new JLabel("Year");
		final JTextField manageMovieYearTxt = new JTextField();
		JLabel manageMovieYearBlank = new JLabel();
		JLabel manageMovieLengthLbl = new JLabel("Length");
		final JTextField manageMovieLengthTxt = new JTextField();
		JLabel manageMovieLengthBlank = new JLabel();
		JLabel manageMovieLanguageLbl = new JLabel("Language");
		final JComboBox manageMovieLanguageCombo = new JComboBox(languagesArray);
		JButton manageMovieLanguageNewBtn = new JButton("Add Language");
		JLabel manageMovieCountryLbl = new JLabel("Country of Origin");
		final JComboBox manageMovieCountryCombo = new JComboBox(countriesArray);
		JButton manageMovieCountryNewBtn = new JButton("Add Country");
		JLabel manageMovieCastLbl = new JLabel("Top Cast");
		final JTextField manageMovieCastTxt = new JTextField();
		JLabel manageMovieCastBlank = new JLabel();
		JLabel manageMoviePlotLbl = new JLabel("Plot Summary");
		final JTextField manageMoviePlotTxt = new JTextField();
		JLabel manageMoviePlotBlank = new JLabel();
		JButton manageEditMovieBtn = new JButton("Edit");
		JButton manageMovieDeleteBtn = new JButton("Delete");
		manageMovieEntriesPanel.add(manageMovieCoverUploadStatusLbl);
		manageMovieEntriesPanel.add(managePlaceHolder2);
		manageMovieEntriesPanel.add(managePlaceholder);
		manageMovieEntriesPanel.add(manageMovieISBNLbl);
		manageMovieEntriesPanel.add(manageMovieISBNTxt);
		manageMovieEntriesPanel.add(manageMovieIDBlank);
		manageMovieEntriesPanel.add(manageMovieTitleLbl);
		manageMovieEntriesPanel.add(manageMovieTitleTxt);
		manageMovieEntriesPanel.add(manageMovieTitleBlank);
		manageMovieEntriesPanel.add(manageMovieDirectorLbl);
		manageMovieEntriesPanel.add(manageMovieDirectorCombo);
		manageMovieEntriesPanel.add(manageMovieDirectorBtn);
		manageMovieEntriesPanel.add(manageMovieGenreLbl);
		manageMovieEntriesPanel.add(manageMovieGenreCombo);
		manageMovieEntriesPanel.add(manageMovieGenreNewBtn);
		manageMovieEntriesPanel.add(manageMovieYearLbl);
		manageMovieEntriesPanel.add(manageMovieYearTxt);
		manageMovieEntriesPanel.add(manageMovieYearBlank);
		manageMovieEntriesPanel.add(manageMovieLengthLbl);
		manageMovieEntriesPanel.add(manageMovieLengthTxt);
		manageMovieEntriesPanel.add(manageMovieLengthBlank);
		manageMovieEntriesPanel.add(manageMovieLanguageLbl);
		manageMovieEntriesPanel.add(manageMovieLanguageCombo);
		manageMovieEntriesPanel.add(manageMovieLanguageNewBtn);
		manageMovieEntriesPanel.add(manageMovieCountryLbl);
		manageMovieEntriesPanel.add(manageMovieCountryCombo);
		manageMovieEntriesPanel.add(manageMovieCountryNewBtn);
		manageMovieEntriesPanel.add(manageMovieCastLbl);
		manageMovieEntriesPanel.add(manageMovieCastTxt);
		manageMovieEntriesPanel.add(manageMovieCastBlank);
		manageMovieEntriesPanel.add(manageMoviePlotLbl);
		manageMovieEntriesPanel.add(manageMoviePlotTxt);
		manageMovieEntriesPanel.add(manageMoviePlotBlank);
		manageMovieEntriesPanel.add(manageEditMovieBtn);
		manageMovieEntriesPanel.add(manageMovieDeleteBtn);
		
		/*text fields for editing or deleting book content on manage page*/
		final JPanel manageAddBookEntriesPanel = new JPanel();
		manageAddBookEntriesPanel.setLayout(new GridLayout(8,3,10,10));
		JLabel manageBookCoverUploadStatusLbl = new JLabel();
		JLabel managePlaceHolder3 = new JLabel();
		JLabel manageBookCoverUploadStatusBlank = new JLabel();
		JLabel manageBookISBNLbl = new JLabel();
		final JTextField manageBookISBNTxt = new JTextField();
		JLabel manageBookISBNBlank = new JLabel();
		JLabel manageBookTitleLbl = new JLabel("Title");
		final JTextField manageBookTitleTxt = new JTextField();
		JLabel manageBookTitleBlank = new JLabel();
		JLabel manageBookAuthorLbl = new JLabel("Author");
		final JComboBox manageBookAuthorCombo = new JComboBox(authorBookArray);
		final JButton manageBookAuthorBtn = new JButton("Add Author");
		JLabel manageBookYearLbl = new JLabel("Year Published");
		final JTextField manageBookYearTxt = new JTextField();
		JLabel manageBookYearBlank = new JLabel();
		JLabel manageBookGenreLbl = new JLabel("Genre");
		final JComboBox manageBookGenreCombo = new JComboBox(genreBookArray);
		final JButton manageBookGenreBtn = new JButton("Add Genre");
		JLabel manageBookPlotLbl = new JLabel("Plot Summary");
		final JTextField manageBookPlotTxt = new JTextField();
		JLabel manageBookPlotBlank = new JLabel();
		JButton manageBookEditBtn = new JButton("Update");
		JButton manageBookDeleteBtn = new JButton("Delete");
		manageAddBookEntriesPanel.add(manageBookCoverUploadStatusLbl);
		manageAddBookEntriesPanel.add(managePlaceHolder3);
		manageAddBookEntriesPanel.add(manageBookCoverUploadStatusBlank);
		manageAddBookEntriesPanel.add(manageBookISBNLbl);
		manageAddBookEntriesPanel.add(manageBookISBNTxt);
		manageAddBookEntriesPanel.add(manageBookISBNBlank);
		manageAddBookEntriesPanel.add(manageBookTitleLbl);
		manageAddBookEntriesPanel.add(manageBookTitleTxt);
		manageAddBookEntriesPanel.add(manageBookTitleBlank);
		manageAddBookEntriesPanel.add(manageBookAuthorLbl);
		manageAddBookEntriesPanel.add(manageBookAuthorCombo);
		manageAddBookEntriesPanel.add(manageBookAuthorBtn);
		manageAddBookEntriesPanel.add(manageBookYearLbl);
		manageAddBookEntriesPanel.add(manageBookYearTxt);
		manageAddBookEntriesPanel.add(manageBookYearBlank);
		manageAddBookEntriesPanel.add(manageBookGenreLbl);
		manageAddBookEntriesPanel.add(manageBookGenreCombo);
		manageAddBookEntriesPanel.add(manageBookGenreBtn);
		manageAddBookEntriesPanel.add(manageBookPlotLbl);
		manageAddBookEntriesPanel.add(manageBookPlotTxt);
		manageAddBookEntriesPanel.add(manageBookPlotBlank);
		manageAddBookEntriesPanel.add(manageBookEditBtn);
		manageAddBookEntriesPanel.add(manageBookDeleteBtn);
		
		/*text fields for editing or deleting CD content on manage page*/
		final JPanel manageAddCDEntriesPanel = new JPanel();
		manageAddCDEntriesPanel.setLayout(new GridLayout(7,3,10,10));
		JLabel manageCDCoverUploadStatusLbl = new JLabel();
		JLabel managePlaceHolder5 = new JLabel();
		JLabel managePlaceholder2 = new JLabel();
		JLabel manageCDISBNLbl = new JLabel("ISBN");
		final JTextField manageCDISBNTxt = new JTextField();
		JLabel manageCDISBNBlank = new JLabel();
		JLabel manageCDArtistLbl = new JLabel("Artist/Band");
		final JComboBox manageCDArtistCombo = new JComboBox(authorCDArray);
		JLabel manageCDArtistBlank = new JLabel();
		JLabel manageCDAlbumLbl = new JLabel("Album Title");
		final JTextField manageCDAlbumTxt = new JTextField();
		JLabel manageCDAlbumBlank = new JLabel();
		JLabel manageCDGenreLbl = new JLabel("Genre");
		final JComboBox manageCDGenreCombo = new JComboBox(genreCDArray);
		JButton manageCDGenreNewBtn = new JButton("Add Genre");
		JButton manageCDEditBtn = new JButton("Update");
		JButton manageCDDeleteBtn = new JButton("Delete");
		manageAddCDEntriesPanel.add(manageCDCoverUploadStatusLbl);
		manageAddCDEntriesPanel.add(managePlaceHolder5);
		manageAddCDEntriesPanel.add(managePlaceholder2);
		manageAddCDEntriesPanel.add(manageCDISBNLbl);
		manageAddCDEntriesPanel.add(manageCDISBNTxt);
		manageAddCDEntriesPanel.add(manageCDISBNBlank);
		manageAddCDEntriesPanel.add(manageCDArtistLbl);
		manageAddCDEntriesPanel.add(manageCDArtistCombo);
		manageAddCDEntriesPanel.add(manageCDArtistBlank);
		manageAddCDEntriesPanel.add(manageCDAlbumLbl);
		manageAddCDEntriesPanel.add(manageCDAlbumTxt);
		manageAddCDEntriesPanel.add(manageCDAlbumBlank);
		manageAddCDEntriesPanel.add(manageCDGenreLbl);
		manageAddCDEntriesPanel.add(manageCDGenreCombo);
		manageAddCDEntriesPanel.add(manageCDGenreNewBtn);
		manageAddCDEntriesPanel.add(manageCDEditBtn);
		manageAddCDEntriesPanel.add(manageCDDeleteBtn);
		
		/*popup to allow new genres*/
		final JPanel addGenrePanel = new JPanel();
		addGenrePanel.setLayout(new FlowLayout());
		final JTextField addGenreTxt = new JTextField();
		JButton addGenreBtn = new JButton("Add");
		JLabel addGenreLbl = new JLabel();
		addGenrePanel.add(addGenreTxt);
		addGenrePanel.add(addGenreBtn);
		addGenrePanel.add(addGenreLbl);
		addGenreLbl.setVisible(false);
		
		/*popup to allow new languages*/
		final JPanel addLanguagePanel = new JPanel();
		addLanguagePanel.setLayout(new FlowLayout());
		final JTextField addLanguageTxt = new JTextField();
		JButton addLanguageBtn = new JButton("Add");
		JLabel addLanguageLbl = new JLabel();
		addLanguagePanel.add(addLanguageTxt);
		addLanguagePanel.add(addLanguageBtn);
		addLanguagePanel.add(addLanguageLbl);
		addLanguageLbl.setVisible(false);
		
		/*popup to allow new countries*/
		final JPanel addCountryPanel = new JPanel();
		addCountryPanel.setLayout(new FlowLayout());
		final JTextField addCountryTxt = new JTextField();
		JButton addCountryBtn = new JButton("Add");
		JLabel addCountryLbl = new JLabel();
		addCountryPanel.add(addCountryTxt);
		addCountryPanel.add(addCountryBtn);
		addCountryPanel.add(addCountryLbl);
		addCountryLbl.setVisible(false);
		
		/*add panels to the tabs*/
		tabbedPane.addTab("Add", addPanel);
		tabbedPane.addTab("Lookup", lookupPanel);
		tabbedPane.addTab("Manage", managePanel);
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		getContentPane().add(topPanel);
		topPanel.add(tabbedPane, BorderLayout.CENTER);
		
		//********************end GUI creation********************************
		//**********************begin actions*********************************
        /* Using this to copy paste for now to add actionListeners
		button.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
            }
        });
        */
		
		/*handles the radio buttons reacting to what is selected*/
		ItemListener radioButtonChanged = new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent itemEvent){
				//String s = (String)itemEvent.getItem();
					if(movieOptionBtn.isSelected()){
						addBookEntriesPanel.setVisible(false);
						addCDEntriesPanel.setVisible(false);
						addMovieEntriesPanel.setVisible(true);
						
					}
					if(bookOptionBtn.isSelected()){
						addCDEntriesPanel.setVisible(false);
						addMovieEntriesPanel.setVisible(false);
						addBookEntriesPanel.setVisible(true);
						
					}
					if(cdOptionBtn.isSelected()){
						addMovieEntriesPanel.setVisible(false);
						addBookEntriesPanel.setVisible(false);
						addCDEntriesPanel.setVisible(true);
						
					}
			}

		};	
		movieOptionBtn.addItemListener(radioButtonChanged);
		bookOptionBtn.addItemListener(radioButtonChanged);
		cdOptionBtn.addItemListener(radioButtonChanged);
		
		/*trigger popup when add by isbn is clicked*/
		addByISBNBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(imagePopupPanel);
                frame.pack();
                frame.setTitle("Image import");
                frame.setVisible(true);
            }
        });
		
		/*trigger popup when add by image recognition is clicked*/
		addByImageRecBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(imagePopupPanel);
                frame.pack();
                frame.setTitle("Image import");
                frame.setVisible(true);
            }
        });
		
		/*trigger popup when import cover is clicked*/
		importCoverBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(imagePopupPanel);
                frame.pack();
                frame.setTitle("Image import");
                frame.setVisible(true);
            }
        });
		
		/*trigger popup when search by photo is clicked*/
		searchByPhotoBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(imagePopupPanel);
                frame.pack();
                frame.setTitle("Image import");
                frame.setVisible(true);
            }
        });
		
		/*trigger popup when search by barcode is clicked*/
		manageSearchByBarcodeBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(imagePopupPanel);
                frame.pack();
                frame.setTitle("Image import");
                frame.setVisible(true);
            }
        });		
		
		/*trigger popup when search by cover photo is clicked*/
		manageSearchByCoverPhotoBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(imagePopupPanel);
                frame.pack();
                frame.setTitle("Image import");
                frame.setVisible(true);
            }
        });
		
		//*********************TODO:Add movie btn*************************
		addMovieBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Connection conn = connection(databaseFilePath);
            	String ISBN = movieISBNTxt.getText();
            	String title = movieTitleTxt.getText();
            	String director = (String) movieDirectorCombo.getSelectedItem();
            	String genre = (String) movieGenreCombo.getSelectedItem();
            	String year = movieYearTxt.getText();
            	String length = movieLengthTxt.getText();
            	String language = (String) movieLanguageCombo.getSelectedItem();
            	String country = (String) movieCountryCombo.getSelectedItem();
            	String cast = movieCastTxt.getText();
            	String plot = moviePlotTxt.getText();
            	
            	//TODO add parameters once constructors are made
            	Movie movie = new Movie();
            	Database.addMovie(movie, conn);
            	addMovieStatusLbl.setText("Your movie " + title + " has been added!");
            }
        });
		
		//*********************TODO:Add book btn*************************
		addBookBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Connection conn = connection(databaseFilePath);
            	String ISBN = bookISBNTxt.getText();
            	String title = bookTitleTxt.getText();
            	String author = (String) bookAuthorCombo.getSelectedItem();
            	String yearPublished = bookYearTxt.getText();
            	String plot = bookPlotTxt.getText();
            	
            	//TODO add parameters once constructors are made
            	Book book = new Book();
            	Database.addBook(book, conn);
            	addBookStatusLbl.setText("Your book " + title + " has been added!");
            }
        });
		
		//*********************TODO:Add cd btn*************************
		addCDBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Connection conn = connection(databaseFilePath);
            	
            	String ISBN = CDISBNTxt.getText();
            	String artist = (String) CDArtistCombo.getSelectedItem();
            	String album = CDAlbumTxt.getText();
            	String genre = (String)CDGenreCombo.getSelectedItem();
            	
            	//TODO add parameters once constructors are made
            	CD cd = new CD();
            	Database.addCD(cd, conn);
            	addCDStatusLbl.setText("Your album " + album + " has been added!");
            }
        });		
		
		/*********************Imports an image to the add media tab*****************************/
		importImageBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String picPath = "";
            	final JFileChooser fc = new JFileChooser();
            	int returnVal = fc.showOpenDialog(importImageBtn);
            	if(returnVal == JFileChooser.APPROVE_OPTION){
            		File file = fc.getSelectedFile();
            		picPath = file.getPath();
            		System.out.println(picPath);
            		try {
            			Image img = ImageIO.read(fc.getSelectedFile());
						Image resizedImage = img.getScaledInstance(150, -1, Image.SCALE_SMOOTH);
						ImageIcon chosenPicture = new ImageIcon(file.getPath());
						if(addPanel.isVisible()){
							if(addMovieEntriesPanel.isVisible()){
								movieCoverUploadStatusLbl.setIcon(new ImageIcon(resizedImage));
								addMovieEntriesPanel.validate();
							}
							else if(addBookEntriesPanel.isVisible()){
								bookCoverUploadStatusLbl.setIcon(new ImageIcon(resizedImage));
								addBookEntriesPanel.validate();
							}
							else if(addCDEntriesPanel.isVisible()){
								CDCoverUploadStatusLbl.setIcon(new ImageIcon(resizedImage));
								addCDEntriesPanel.validate();
							}
							else{
								JLabel warning = new JLabel("You have not selected your media type yet!");
								JFrame frame = new JFrame();
				                frame.setLayout(new FlowLayout());
				                frame.add(warning);
				                frame.pack();
				                frame.setTitle("Warning!");
				                frame.setVisible(true);
							}
						}
						else if(lookupPanel.isVisible()){
							//TODO implement image search by recognition and prompt search results
						}
						else if(managePanel.isVisible()){
							//TODO implement image search by recognition and prompt edit option
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
            		
            	}
            }
        });		
		
		//*********************TODO:Take photo btn*************************
		takePhotoBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
            }
        });	
		
		//*********************TODO:Search "enter" btn*************************
		searchEnterBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
            }
        });		
		
		//*********************TODO:Search "display all" btn*************************
		searchDisplayAllBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Connection conn = connection(databaseFilePath);
        		
            	Movie[] movieArray = search.getAllMovies(conn);
            	Book[] bookArray = search.getAllBooks(conn);
            	CD[] CDArray = search.getAllCDs(conn);
            	
            }
        });	
		/*
		//*********************TODO:Manage "enter" btn*************************
		manageEnterBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Media searchResults;
            	searchResults = DBController.search();
            	JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                if(searchResults instanceof Movie)
                	frame.add(manageMovieEntriesPanel);
                else if(searchResults instanceof Book)
                	frame.add(manageAddBookEntriesPanel);
                else if(searchResults instanceof CD)
                	frame.add(manageAddCDEntriesPanel);
                frame.pack();
                frame.setTitle("Edit");
                frame.setVisible(true);
            }
        });		
		*/
		//*********************TODO:Manage "Add CD genre" btn*************************
		CDGenreNewBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(addGenrePanel);
                frame.pack();
                frame.setTitle("Add Genre");
                frame.setVisible(true);
            }
        });	

		/*adds the genre as an option to the database*/
		movieGenreNewBtn.addActionListener(new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	            Connection conn = connection(databaseFilePath);
	        	String newGenre = addGenreTxt.getText();
	            try{
		            Statement stat = conn.createStatement();
		            String sql = "INSERT INTO genre VALUES(" + newGenre + ")";
		            stat.executeUpdate(sql);
	            }
	            catch (SQLException e) {
	            	System.out.println("Error in entering the new Genre value");
				}
	        }    
	    });	
		
		
		/*popup to add a genre*/
		movieGenreNewBtn.addActionListener(new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(addGenrePanel);
                frame.pack();
                frame.setTitle("Add Genre");
                frame.setVisible(true);
	        }    
	    });	

		/*popup to add a genre database*/
		movieLanguageNewBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(addLanguagePanel);
                frame.pack();
                frame.setTitle("Add Language");
                frame.setVisible(true);
            }
        });	
		
		/*popup to add a country to the database*/
		movieCountryNewBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(addCountryPanel);
                frame.pack();
                frame.setTitle("Add Country");
                frame.setVisible(true);
            }
        });	
		
		/*add a language to the database*/
		addLanguageBtn.addActionListener(new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	Connection conn = connection(databaseFilePath);
	        	String newLanguage = addLanguageTxt.getText();
	            Statement stat;
				try {
					stat = conn.createStatement();
					String sql = "INSERT INTO language VALUES(" + newLanguage + ")";
		            stat.executeUpdate(sql);
				} catch (SQLException e) {
					System.out.println("Error in entering the new Language value");
				}
	        }    
	    });	
		
		/*add a country to the database*/
		addCountryBtn.addActionListener(new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	Connection conn = connection(databaseFilePath);
	        	String newCountry = addCountryTxt.getText();
	            Statement stat;
				try {
					stat = conn.createStatement();
					String sql = "INSERT INTO country VALUES(" + newCountry + ")";
		            stat.executeUpdate(sql);
				} catch (SQLException e) {
					System.out.println("Error in entering the new Country value");
				}
	        }    
	    });	
		
		//*********************TODO:Update movie entry btn*************************
		manageEditMovieBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Connection conn = connection(databaseFilePath);
            	
            	String ISBN = manageMovieISBNTxt.getText();
            	String title = manageMovieTitleTxt.getText();
            	String director = (String) manageMovieDirectorCombo.getSelectedItem();
            	String genre = (String) manageMovieGenreCombo.getSelectedItem();
            	String year = manageMovieYearTxt.getText();
            	String length = manageMovieLengthTxt.getText();
            	String language = (String) manageMovieLanguageCombo.getSelectedItem();
            	String country = (String) manageMovieCountryCombo.getSelectedItem();
            	String cast = manageMovieCastTxt.getText();
            	String plot = manageMoviePlotTxt.getText();
            	
            	//TODO add parameters once constructors are made
            	Movie movie = new Movie();
            	try {
					Database.update(movie, conn);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });	
		
		//*********************TODO:Delete movie entry btn*************************
		manageMovieDeleteBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Connection conn = connection(databaseFilePath);
            	
            	String ISBN = manageMovieISBNTxt.getText();
            	String title = manageMovieTitleTxt.getText();
            	String director = (String) manageMovieDirectorCombo.getSelectedItem();
            	String genre = (String) manageMovieGenreCombo.getSelectedItem();
            	String year = manageMovieYearTxt.getText();
            	String length = manageMovieLengthTxt.getText();
            	String language = (String) manageMovieLanguageCombo.getSelectedItem();
            	String country = (String) manageMovieCountryCombo.getSelectedItem();
            	String cast = manageMovieCastTxt.getText();
            	String plot = manageMoviePlotTxt.getText();
            	
            	//TODO add parameters once constructors are made
            	Movie movie = new Movie();
            	try {
					Database.delete(movie, conn);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });	
		
		//*********************TODO:Update book entry btn*************************
		manageBookEditBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Connection conn = connection(databaseFilePath);
            	
            	String ISBN = manageBookISBNTxt.getText();
            	String title = manageBookTitleTxt.getText();
            	String author = (String) manageBookAuthorCombo.getSelectedItem();
            	String yearPublished = manageBookYearTxt.getText();
            	String plot = manageBookPlotTxt.getText();
            	
            	//TODO add parameters once constructors are made
            	Book book = new Book();
            	try {
					Database.update(book, conn);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });	
		
		//*********************TODO:Delete book entry btn*************************
		manageBookDeleteBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Connection conn = connection(databaseFilePath);
            	
            	String ISBN = manageBookISBNTxt.getText();
            	String title = manageBookTitleTxt.getText();
            	String author = (String) manageBookAuthorCombo.getSelectedItem();
            	String yearPublished = manageBookYearTxt.getText();
            	String plot = manageBookPlotTxt.getText();
            	
            	//TODO add parameters once constructors are made
            	Book book = new Book();
            	try {
					Database.delete(book, conn);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });	
		
		//*********************TODO:Update CD entry btn*************************
		manageCDEditBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Connection conn = connection(databaseFilePath);
            	
            	String ISBN = manageCDISBNTxt.getText();
            	String artist = (String) manageCDArtistCombo.getSelectedItem();
            	String album = manageCDAlbumTxt.getText();
            	String genre = (String)manageCDGenreCombo.getSelectedItem();
            	
            	//TODO add parameters once constructors are made
            	CD cd = new CD();
            	try {
					Database.update(cd, conn);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });	
		
		//*********************TODO:Delete CD entry btn*************************
		manageCDDeleteBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Connection conn = connection(databaseFilePath);
            	
            	String ISBN = manageCDISBNTxt.getText();
            	String artist = (String) manageCDArtistCombo.getSelectedItem();
            	String album = manageCDAlbumTxt.getText();
            	String genre = (String)manageCDGenreCombo.getSelectedItem();
            	
            	//TODO add parameters once constructors are made
            	CD cd = new CD();
            	try {
					Database.delete(cd, conn);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });	
	    
	}

	public static void main(String[] args) {
		mediaLibrary frame = new mediaLibrary();
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(new Dimension(800,700));
		frame.setTitle("Media Library");
		frame.setVisible(true);
	}

}
