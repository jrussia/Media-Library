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
 * @version 1.1.2 2/28/15
 */

public class mediaLibrary extends JFrame{
	
	public Connection connection(String filePath){
		String myFilePath = "C:/Users/User/workspace/mediaLibrary/database.db";
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
	
	public mediaLibrary(){
		//********************connect with the database with SQLite***********************
		LinkedList countryList = new LinkedList();
		LinkedList languageList = new LinkedList();
		LinkedList genreList = new LinkedList();
		Connection conn = connection("C:/Users/User/workspace/mediaLibrary/database.db");
			try {
				conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/User/workspace/mediaLibrary/database.db");
				Statement stat = conn.createStatement();
				ResultSet rsCountry = stat.executeQuery("select * from country");
				while(rsCountry.next()){
					countryList.add(rsCountry.getString("country"));
				}
				ResultSet rsLanguage = stat.executeQuery("select * from language");
				while(rsLanguage.next()){
					languageList.add(rsLanguage.getString("language"));
				}
				ResultSet rsGenre = stat.executeQuery("select * from genre");
				while(rsGenre.next()){
					genreList.add(rsGenre.getString("genre"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
		}
			//System.out.println(countryList.size()+ " " +languageList.size()+ " " +genreList.size());
			String [] countriesArray = new String[countryList.size()];
			String [] languagesArray = new String[languageList.size()];
			String [] genreArray = new String[genreList.size()];
			for(int i = 0; i < countryList.size(); i++){
				countriesArray[i] = (String) countryList.get(i);
			}
			for(int i = 0; i < languageList.size(); i++){
				languagesArray[i] = (String) languageList.get(i);
			}
			for(int i = 0; i < genreList.size(); i++){
				genreArray[i] = (String) genreList.get(i);
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
		addMovieEntriesPanel2.setLayout(new GridLayout(12, 3, 10, 10));
		final JLabel movieCoverUploadStatusLbl = new JLabel();
		JLabel movieIDLbl = new JLabel("ID");
		JLabel movieID = new JLabel();
		JLabel movieIDBlank = new JLabel();
		JLabel movieTitleLbl = new JLabel("Title");
		JTextField movieTitleTxt = new JTextField();
		JLabel movieTitleBlank = new JLabel();
		JLabel movieDirectorLbl = new JLabel("Director");
		JTextField movieDirectorTxt = new JTextField();
		JLabel movieDirectorBlank = new JLabel();
		JLabel movieGenreLbl = new JLabel("Genre");
		JComboBox movieGenreCombo = new JComboBox(genreArray);
		JButton movieGenreNewBtn = new JButton("Add Genre");
		JLabel movieYearLbl = new JLabel("Year");
		JTextField movieYearTxt = new JTextField();
		JLabel movieYearBlank = new JLabel();
		JLabel movieLengthLbl = new JLabel("Length");
		JTextField movieLengthTxt = new JTextField();
		JLabel movieLengthBlank = new JLabel();
		JLabel movieLanguageLbl = new JLabel("Language");
		JComboBox movieLanguageCombo = new JComboBox(languagesArray);
		JButton movieLanguageNewBtn = new JButton("Add Language");
		JLabel movieCountryLbl = new JLabel("Country of Origin");
		JComboBox movieCountryCombo = new JComboBox(countriesArray);
		JButton movieCountryNewBtn = new JButton("Add Country");
		JLabel movieCastLbl = new JLabel("Top Cast");
		JTextField movieCastTxt = new JTextField();
		JLabel movieCastBlank = new JLabel();
		JLabel moviePlotLbl = new JLabel("Plot Summary");
		JTextField moviePlotTxt = new JTextField();
		JLabel moviePlotBlank = new JLabel();
		JLabel placeHolder1 = new JLabel();
		JButton addMovieBtn = new JButton("Add");
		addMovieEntriesPanel2.add(movieIDLbl);
		addMovieEntriesPanel2.add(movieID);
		addMovieEntriesPanel2.add(movieIDBlank);
		addMovieEntriesPanel2.add(movieTitleLbl);
		addMovieEntriesPanel2.add(movieTitleTxt);
		addMovieEntriesPanel2.add(movieTitleBlank);
		addMovieEntriesPanel2.add(movieDirectorLbl);
		addMovieEntriesPanel2.add(movieDirectorTxt);
		addMovieEntriesPanel2.add(movieDirectorBlank);
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
		addMovieEntriesPanel.add(movieCoverUploadStatusLbl);
		addMovieEntriesPanel.add(addMovieEntriesPanel2);
		
		/*text fields for adding book content*/
		final JPanel addBookEntriesPanel2 = new JPanel();
		addBookEntriesPanel2.setLayout(new GridLayout(5,2,10,10));
		final JPanel addBookEntriesPanel = new JPanel();
		addBookEntriesPanel.setLayout(new FlowLayout());
		final JLabel bookCoverUploadStatusLbl = new JLabel();
		JLabel bookTitleLbl = new JLabel("Title");
		JTextField bookTitleTxt = new JTextField();
		JLabel bookAuthorLbl = new JLabel("Author");
		JTextField bookAuthorTxt = new JTextField();
		JLabel bookYearLbl = new JLabel("Year Published");
		JTextField bookYearTxt = new JTextField();
		JLabel bookPlotLbl = new JLabel("Plot Summary");
		JTextField bookPlotTxt = new JTextField();
		JPanel placeHolder4 = new JPanel();
		JButton addBookBtn = new JButton("Add");
		addBookEntriesPanel2.add(bookTitleLbl);
		addBookEntriesPanel2.add(bookTitleTxt);
		addBookEntriesPanel2.add(bookAuthorLbl);
		addBookEntriesPanel2.add(bookAuthorTxt);
		addBookEntriesPanel2.add(bookYearLbl);
		addBookEntriesPanel2.add(bookYearTxt);
		addBookEntriesPanel2.add(bookPlotLbl);
		addBookEntriesPanel2.add(bookPlotTxt);
		addBookEntriesPanel2.add(placeHolder4);
		addBookEntriesPanel2.add(addBookBtn);
		addBookEntriesPanel.add(bookCoverUploadStatusLbl);
		addBookEntriesPanel.add(addBookEntriesPanel2);
		
		/*text fields for adding CD content*/
		final JPanel addCDEntriesPanel2 = new JPanel();
		addCDEntriesPanel2.setLayout(new GridLayout(5,3,10,10));
		final JPanel addCDEntriesPanel = new JPanel();
		addCDEntriesPanel.setLayout(new FlowLayout());
		final JLabel CDCoverUploadStatusLbl = new JLabel();
		JLabel placeHolder5 = new JLabel();
		JLabel placeholder2 = new JLabel();
		JLabel CDArtistLbl = new JLabel("Artist/Band");
		JTextField CDArtistTxt = new JTextField();
		JLabel CDArtistBlank = new JLabel();
		JLabel CDAlbumLbl = new JLabel("Album Title");
		JTextField CDAlbumTxt = new JTextField();
		JLabel CDAlbumBlank = new JLabel();
		JLabel CDGenreLbl = new JLabel("Genre");
		JComboBox CDGenreCombo = new JComboBox(genreArray);
		JButton CDGenreNewBtn = new JButton("Add Genre");
		JLabel placeHolder6 = new JLabel();
		JButton addCDBtn = new JButton("Add");
		addCDEntriesPanel2.add(CDArtistLbl);
		addCDEntriesPanel2.add(CDArtistTxt);
		addCDEntriesPanel2.add(CDArtistBlank);
		addCDEntriesPanel2.add(CDAlbumLbl);
		addCDEntriesPanel2.add(CDAlbumTxt);
		addCDEntriesPanel2.add(CDAlbumBlank);
		addCDEntriesPanel2.add(CDGenreLbl);
		addCDEntriesPanel2.add(CDGenreCombo);
		addCDEntriesPanel2.add(CDGenreNewBtn);
		addCDEntriesPanel2.add(placeHolder6);
		addCDEntriesPanel2.add(addCDBtn);
		addCDEntriesPanel.add(CDCoverUploadStatusLbl);
		addCDEntriesPanel.add(addCDEntriesPanel2);
		
		/*buttons to edit or delete the field present*/
		JPanel editDeletePanel = new JPanel();
		editDeletePanel.setLayout(new FlowLayout());
		JButton updateEntryBtn = new JButton("Update");
		JButton deleteEntryBtn = new JButton("Delete");
		editDeletePanel.add(updateEntryBtn);
		editDeletePanel.add(deleteEntryBtn);
		
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
		addPanel.add(editDeletePanel, BorderLayout.SOUTH);
		addPanel.add(addButtonsPanel, BorderLayout.NORTH);
		addPanel.add(addCenterPanel, BorderLayout.CENTER);
		editDeletePanel.setVisible(false);
		
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
		String[] movieColumnNames = {"Cover", "Title", "Director", "Genre", "Year", "Length", "Language", "Country", "Cast", "Plot"};
		Object[][] movieTableData = {};
		JTable movieTable = new JTable(movieTableData, movieColumnNames);
		JScrollPane movieTableScrollPane = new JScrollPane(movieTable);
		movieTableScrollPane.setPreferredSize(getPreferredSize());
		JLabel tablesBooksLbl = new JLabel("Books");
		String[] bookColumnNames = {"Cover", "Title", "Author", "Year Published", "Plot"};
		Object[][] bookTableData = {};
		JTable bookTable = new JTable(bookTableData, bookColumnNames);
		JScrollPane bookTableScrollPane = new JScrollPane(bookTable);
		bookTableScrollPane.setPreferredSize(getPreferredSize());
		JLabel tablesCDLbl = new JLabel("CDs");
		String[] CDColumnNames = {"Cover", "Artist/Band", "Album Title", "Genre"};
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
		/*Image image = new Image(getClass().getResourceAsStream(filePath));*/
		JLabel managePlaceHolder2 = new JLabel();
		JLabel managePlaceholder = new JLabel();
		JLabel manageMovieIDLbl = new JLabel("ID");
		JLabel manageMovieID = new JLabel();
		JLabel manageMovieIDBlank = new JLabel();
		JLabel manageMovieTitleLbl = new JLabel("Title");
		JTextField manageMovieTitleTxt = new JTextField();
		JLabel manageMovieTitleBlank = new JLabel();
		JLabel manageMovieDirectorLbl = new JLabel("Director");
		JTextField manageMovieDirectorTxt = new JTextField();
		JLabel manageMovieDirectorBlank = new JLabel();
		JLabel manageMovieGenreLbl = new JLabel("Genre");
		JComboBox manageMovieGenreCombo = new JComboBox(genreArray);
		JButton manageMovieGenreNewBtn = new JButton("Add Genre");
		JLabel manageMovieYearLbl = new JLabel("Year");
		JTextField manageMovieYearTxt = new JTextField();
		JLabel manageMovieYearBlank = new JLabel();
		JLabel manageMovieLengthLbl = new JLabel("Length");
		JTextField manageMovieLengthTxt = new JTextField();
		JLabel manageMovieLengthBlank = new JLabel();
		JLabel manageMovieLanguageLbl = new JLabel("Language");
		JComboBox manageMovieLanguageCombo = new JComboBox(languagesArray);
		JButton manageMovieLanguageNewBtn = new JButton("Add Language");
		JLabel manageMovieCountryLbl = new JLabel("Country of Origin");
		JComboBox manageMovieCountryCombo = new JComboBox(countriesArray);
		JButton manageMovieCountryNewBtn = new JButton("Add Country");
		JLabel manageMovieCastLbl = new JLabel("Top Cast");
		JTextField manageMovieCastTxt = new JTextField();
		JLabel manageMovieCastBlank = new JLabel();
		JLabel manageMoviePlotLbl = new JLabel("Plot Summary");
		JTextField manageMoviePlotTxt = new JTextField();
		JLabel manageMoviePlotBlank = new JLabel();
		JButton manageEditMovieBtn = new JButton("Edit");
		JButton manageMovieDeleteBtn = new JButton("Delete");
		manageMovieEntriesPanel.add(manageMovieCoverUploadStatusLbl);
		manageMovieEntriesPanel.add(managePlaceHolder2);
		manageMovieEntriesPanel.add(managePlaceholder);
		manageMovieEntriesPanel.add(manageMovieIDLbl);
		manageMovieEntriesPanel.add(manageMovieID);
		manageMovieEntriesPanel.add(manageMovieIDBlank);
		manageMovieEntriesPanel.add(manageMovieTitleLbl);
		manageMovieEntriesPanel.add(manageMovieTitleTxt);
		manageMovieEntriesPanel.add(manageMovieTitleBlank);
		manageMovieEntriesPanel.add(manageMovieDirectorLbl);
		manageMovieEntriesPanel.add(manageMovieDirectorTxt);
		manageMovieEntriesPanel.add(manageMovieDirectorBlank);
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
		manageAddBookEntriesPanel.setLayout(new GridLayout(6,2,10,10));
		JLabel manageBookCoverUploadStatusLbl = new JLabel();
		JLabel managePlaceHolder3 = new JLabel();
		JLabel manageBookTitleLbl = new JLabel("Title");
		JTextField manageBookTitleTxt = new JTextField();
		JLabel manageBookAuthorLbl = new JLabel("Author");
		JTextField manageBookAuthorTxt = new JTextField();
		JLabel manageBookYearLbl = new JLabel("Year Published");
		JTextField manageBookYearTxt = new JTextField();
		JLabel manageBookPlotLbl = new JLabel("Plot Summary");
		JTextField manageBookPlotTxt = new JTextField();
		JButton manageBookEditBtn = new JButton("Update");
		JButton manageBookDeleteBtn = new JButton("Delete");
		manageAddBookEntriesPanel.add(manageBookCoverUploadStatusLbl);
		manageAddBookEntriesPanel.add(managePlaceHolder3);
		manageAddBookEntriesPanel.add(manageBookTitleLbl);
		manageAddBookEntriesPanel.add(manageBookTitleTxt);
		manageAddBookEntriesPanel.add(manageBookAuthorLbl);
		manageAddBookEntriesPanel.add(manageBookAuthorTxt);
		manageAddBookEntriesPanel.add(manageBookYearLbl);
		manageAddBookEntriesPanel.add(manageBookYearTxt);
		manageAddBookEntriesPanel.add(manageBookPlotLbl);
		manageAddBookEntriesPanel.add(manageBookPlotTxt);
		manageAddBookEntriesPanel.add(manageBookEditBtn);
		manageAddBookEntriesPanel.add(manageBookDeleteBtn);
		
		/*text fields for editing or deleting CD content on manage page*/
		final JPanel manageAddCDEntriesPanel = new JPanel();
		manageAddCDEntriesPanel.setLayout(new GridLayout(6,3,10,10));
		JLabel manageCDCoverUploadStatusLbl = new JLabel();
		JLabel managePlaceHolder5 = new JLabel();
		JLabel managePlaceholder2 = new JLabel();
		JLabel manageCDArtistLbl = new JLabel("Artist/Band");
		JTextField manageCDArtistTxt = new JTextField();
		JLabel manageCDArtistBlank = new JLabel();
		JLabel manageCDAlbumLbl = new JLabel("Album Title");
		JTextField manageCDAlbumTxt = new JTextField();
		JLabel manageCDAlbumBlank = new JLabel();
		JLabel manageCDGenreLbl = new JLabel("Genre");
		JComboBox manageCDGenreCombo = new JComboBox(genreArray);
		JButton manageCDGenreNewBtn = new JButton("Add Genre");
		JButton manageCDEditBtn = new JButton("Update");
		JButton manageCDDeleteBtn = new JButton("Delete");
		manageAddCDEntriesPanel.add(manageCDCoverUploadStatusLbl);
		manageAddCDEntriesPanel.add(managePlaceHolder5);
		manageAddCDEntriesPanel.add(managePlaceholder2);
		manageAddCDEntriesPanel.add(manageCDArtistLbl);
		manageAddCDEntriesPanel.add(manageCDArtistTxt);
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
                
            }
        });
		
		//*********************TODO:Add book btn*************************
		addBookBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
            }
        });
		
		//*********************TODO:Add cd btn*************************
		addCDBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
            }
        });		
		
		//*********************TODO:Import image btn*************************
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
                
            }
        });	
/*		
		//*********************TODO:Manage "enter" btn*************************
		manageEnterBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Media searchResults;
            	searchResults = DBController.searchDatabase();
            	JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                if(Media instanceof Movie)
                	frame.add(manageMovieEntriesPanel);
                else if(Media instanceof Book)
                	frame.add(manageAddBookEntriesPanel);
                else if(Media instanceof CD)
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

		//*********************TODO:Manage "Add CD genre enter" btn*************************
		movieGenreNewBtn.addActionListener(new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	            Connection conn = connection("C:/Users/User/workspace/mediaLibrary/database.db");
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
		
		
		//*********************TODO:Manage "Add movie genre" btn*************************
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
		
		//*********************TODO:Manage "Add movie language btn" btn*************************
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
		
		//*********************TODO:Manage "Add movie country" btn*************************
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
		
		//*********************TODO:Manage "Add language enter" btn*************************
		addLanguageBtn.addActionListener(new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	Connection conn = connection("C:/Users/User/workspace/mediaLibrary/database.db");
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
		
		//*********************TODO:Manage "Add country enter" btn*************************
		addCountryBtn.addActionListener(new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	Connection conn = connection("C:/Users/User/workspace/mediaLibrary/database.db");
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
