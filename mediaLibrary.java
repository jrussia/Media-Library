package media;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;

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
 * @version 1.2.0 3/8/15
 */

public class mediaLibrary extends JFrame{
	public String databaseFilePath = "C:/Users/User/workspace/mediaLibrary/database.db";
	public String imageFilePath = "";
	public FileInputStream fileInputStream = null;
	public byte[] editCover = null;
	
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
		final DefaultComboBoxModel movieDirectorComboModel = new DefaultComboBoxModel(authorMovieArray);
		final JComboBox movieDirectorCombo = new JComboBox(movieDirectorComboModel);
		JButton movieDirectorBtn = new JButton("Add Director");
		JLabel movieGenreLbl = new JLabel("Genre");
		final DefaultComboBoxModel movieGenreComboModel = new DefaultComboBoxModel(genreMovieArray);
		final JComboBox movieGenreCombo = new JComboBox(movieGenreComboModel);
		JButton movieGenreNewBtn = new JButton("Add Genre");
		JLabel movieYearLbl = new JLabel("Year");
		final JTextField movieYearTxt = new JTextField();
		JLabel movieYearBlank = new JLabel();
		JLabel movieLengthLbl = new JLabel("Length (minutes)");
		final JTextField movieLengthTxt = new JTextField();
		JLabel movieLengthBlank = new JLabel();
		JLabel movieLanguageLbl = new JLabel("Language");
		final DefaultComboBoxModel movieLanguageComboModel = new DefaultComboBoxModel(languagesArray);
		final JComboBox movieLanguageCombo = new JComboBox(movieLanguageComboModel);
		JButton movieLanguageNewBtn = new JButton("Add Language");
		JLabel movieCountryLbl = new JLabel("Country of Origin");
		final DefaultComboBoxModel movieCountryComboModel = new DefaultComboBoxModel(countriesArray);
		final JComboBox movieCountryCombo = new JComboBox(movieCountryComboModel);
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
		final DefaultComboBoxModel bookAuthorComboModel = new DefaultComboBoxModel(authorBookArray);
		final JComboBox bookAuthorCombo = new JComboBox(bookAuthorComboModel);
		final JButton bookAuthorBtn = new JButton("Add Author");
		JLabel bookLengthLbl = new JLabel("Length (pages)");
		final JTextField bookLengthTxt = new JTextField();
		JLabel bookLengthBlank = new JLabel();
		JLabel bookYearLbl = new JLabel("Year Published");
		final JTextField bookYearTxt = new JTextField();
		JLabel bookYearBlank = new JLabel();
		JLabel bookGenreLbl = new JLabel("Genre");
		final DefaultComboBoxModel bookGenreComboModel = new DefaultComboBoxModel(genreBookArray);
		final JComboBox bookGenreCombo = new JComboBox(bookGenreComboModel);
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
		final DefaultComboBoxModel CDArtistComboModel = new DefaultComboBoxModel(authorCDArray);
		final JComboBox CDArtistCombo = new JComboBox(CDArtistComboModel);
		final JButton CDArtistBtn = new JButton("Add Author");
		JLabel CDAlbumLbl = new JLabel("Album Title");
		final JTextField CDAlbumTxt = new JTextField();
		JLabel CDAlbumBlank = new JLabel();
		JLabel CDGenreLbl = new JLabel("Genre");
		final DefaultComboBoxModel CDGenreComboModel = new DefaultComboBoxModel(genreCDArray);
		final JComboBox CDGenreCombo = new JComboBox(CDGenreComboModel);
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
		
		/*popup panel to choose how to bring an image in for import cover add tab*/
		final JPanel addImportCoverImagePopupPanel = new JPanel();
		addImportCoverImagePopupPanel.setLayout(new FlowLayout());
		final JButton addImportCoverImportImageBtn = new JButton("Import Image");
		final JButton addImportCoverTakePhotoBtn = new JButton("Take Photo");
		addImportCoverImagePopupPanel.add(addImportCoverImportImageBtn);
		addImportCoverImagePopupPanel.add(addImportCoverTakePhotoBtn);
		
		/*popup panel to choose how to bring an image in for image recognition add tab*/
		final JPanel imageRecognitionPopupPanel = new JPanel();
		imageRecognitionPopupPanel.setLayout(new FlowLayout());
		final JButton imageRecognitionImportImageBtn = new JButton("Import Image");
		final JButton imageRecognitionTakePhotoBtn = new JButton("Take Photo");
		imageRecognitionPopupPanel.add(imageRecognitionImportImageBtn);
		imageRecognitionPopupPanel.add(imageRecognitionTakePhotoBtn);
		
		/*popup panel to choose how to bring an image in for auto ISBN add tab*/
		final JPanel autoISBNImagePopupPanel = new JPanel();
		autoISBNImagePopupPanel.setLayout(new FlowLayout());
		final JButton autoISBNImportImageBtn = new JButton("Import Image");
		final JButton autoISBNTakePhotoBtn = new JButton("Take Photo");
		autoISBNImagePopupPanel.add(autoISBNImportImageBtn);
		autoISBNImagePopupPanel.add(autoISBNTakePhotoBtn);
		
		/*popup panel to choose how to bring an image in for search by photo lookup tab*/
		final JPanel searchImagePopupPanel = new JPanel();
		searchImagePopupPanel.setLayout(new FlowLayout());
		final JButton searchImportImageBtn = new JButton("Import Image");
		final JButton searchTakePhotoBtn = new JButton("Take Photo");
		searchImagePopupPanel.add(searchImportImageBtn);
		searchImagePopupPanel.add(searchTakePhotoBtn);
		
		/*popup panel to choose how to bring an image in for search by barcode manage tab*/
		final JPanel manageBarcodeImagePopupPanel = new JPanel();
		manageBarcodeImagePopupPanel.setLayout(new FlowLayout());
		final JButton manageBarcodeImportImageBtn = new JButton("Import Image");
		final JButton manageBarcodeTakePhotoBtn = new JButton("Take Photo");
		manageBarcodeImagePopupPanel.add(manageBarcodeImportImageBtn);
		manageBarcodeImagePopupPanel.add(manageBarcodeTakePhotoBtn);
		
		/*popup panel to choose how to bring an image in for search by cover photo manage tab*/
		final JPanel manageCoverSearchPopupPanel = new JPanel();
		manageCoverSearchPopupPanel.setLayout(new FlowLayout());
		final JButton manageCoverSearchImageBtn = new JButton("Import Image");
		final JButton manageCoverSearchTakePhotoBtn = new JButton("Take Photo");
		manageCoverSearchPopupPanel.add(manageCoverSearchImageBtn);
		manageCoverSearchPopupPanel.add(manageCoverSearchTakePhotoBtn);
		
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
		String[] movieColumnNames = {"Barcode", "Title", "Director", "Genre", "Year", "Length", "Language", "Country", "Cast", "Plot"};
		Object[][] movieTableData = {};
		final DefaultTableModel movieModel = new DefaultTableModel();
		JTable movieTable = new JTable(movieTableData, movieColumnNames);
		movieModel.setColumnIdentifiers(movieColumnNames);
		movieTable.setModel(movieModel);
		JScrollPane movieTableScrollPane = new JScrollPane(movieTable);
		movieTableScrollPane.setPreferredSize(new Dimension((movieTable.getPreferredSize()).width, (movieTable.getRowHeight()*10)));
		JLabel tablesBooksLbl = new JLabel("Books");
		String[] bookColumnNames = {"ISBN", "Title", "Author", "Genre", "Year Published", "Length", "Plot"};
		Object[][] bookTableData = {};
		final DefaultTableModel bookModel = new DefaultTableModel();
		JTable bookTable = new JTable(bookTableData, bookColumnNames);
		bookModel.setColumnIdentifiers(bookColumnNames);
		bookTable.setModel(bookModel);
		JScrollPane bookTableScrollPane = new JScrollPane(bookTable);
		bookTableScrollPane.setPreferredSize(new Dimension((bookTable.getPreferredSize()).width, (bookTable.getRowHeight()*10)));
		JLabel tablesCDLbl = new JLabel("CDs");
		String[] CDColumnNames = {"Barcode", "Artist/Band", "Album Title", "Genre"};
		Object[][] CDTableData = {};
		final DefaultTableModel cdModel = new DefaultTableModel();
		JTable CDTable = new JTable(CDTableData, CDColumnNames);
		final JLabel searchStatusLbl = new JLabel();
		cdModel.setColumnIdentifiers(CDColumnNames);
		CDTable.setModel(cdModel);
		JScrollPane CDTableScrollPane = new JScrollPane(CDTable);
		CDTableScrollPane.setPreferredSize(new Dimension((CDTable.getPreferredSize()).width, (CDTable.getRowHeight()*10)));
		searchTablesPanel.add(tablesMoviesLbl);
		searchTablesPanel.add(movieTableScrollPane);
		searchTablesPanel.add(tablesBooksLbl);
		searchTablesPanel.add(bookTableScrollPane);
		searchTablesPanel.add(tablesCDLbl);
		searchTablesPanel.add(CDTableScrollPane);
		searchTablesPanel.add(searchStatusLbl);
		
		/*all widgets for the lookup Tab*/
		final JPanel lookupPanel = new JPanel();
		lookupPanel.setLayout(new BorderLayout());
		lookupPanel.add(topSearchPanel, BorderLayout.NORTH);
		lookupPanel.add(searchTablesPanel, BorderLayout.CENTER);
		
		/*top widgets on the manage tab*/
		JPanel manageTopPanel = new JPanel();
		manageTopPanel.setLayout(new GridLayout(1,3,10,10));
		JLabel manageSearchByISBNLbl = new JLabel("Search by ISBN");
		final JTextField manageSearchByISBNTxt = new JTextField();
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
		final JLabel manageMovieCoverUploadStatusLbl = new JLabel();
		JLabel managePlaceHolder2 = new JLabel();
		JLabel managePlaceholder = new JLabel();
		JLabel manageMovieISBNLbl = new JLabel("ISBN");
		final JTextField manageMovieISBNTxt = new JTextField();
		JLabel manageMovieIDBlank = new JLabel();
		JLabel manageMovieTitleLbl = new JLabel("Title");
		final JTextField manageMovieTitleTxt = new JTextField();
		JLabel manageMovieTitleBlank = new JLabel();
		JLabel manageMovieDirectorLbl = new JLabel("Director");
		final DefaultComboBoxModel manageMovieDirectorComboModel = new DefaultComboBoxModel(authorMovieArray);
		final JComboBox manageMovieDirectorCombo = new JComboBox(manageMovieDirectorComboModel);
		JButton manageMovieDirectorBtn = new JButton("Add Director");
		JLabel manageMovieGenreLbl = new JLabel("Genre");
		final DefaultComboBoxModel manageMovieGenreComboModel = new DefaultComboBoxModel(genreMovieArray);
		final JComboBox manageMovieGenreCombo = new JComboBox(manageMovieGenreComboModel);
		JButton manageMovieGenreNewBtn = new JButton("Add Genre");
		JLabel manageMovieYearLbl = new JLabel("Year");
		final JTextField manageMovieYearTxt = new JTextField();
		JLabel manageMovieYearBlank = new JLabel();
		JLabel manageMovieLengthLbl = new JLabel("Length");
		final JTextField manageMovieLengthTxt = new JTextField();
		JLabel manageMovieLengthBlank = new JLabel();
		JLabel manageMovieLanguageLbl = new JLabel("Language");
		final DefaultComboBoxModel manageMovieLanguageComboModel = new DefaultComboBoxModel(languagesArray);
		final JComboBox manageMovieLanguageCombo = new JComboBox(manageMovieLanguageComboModel);
		JButton manageMovieLanguageNewBtn = new JButton("Add Language");
		JLabel manageMovieCountryLbl = new JLabel("Country of Origin");
		final DefaultComboBoxModel manageMovieCountryComboModel = new DefaultComboBoxModel(countriesArray);
		final JComboBox manageMovieCountryCombo = new JComboBox(manageMovieCountryComboModel);
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
		manageAddBookEntriesPanel.setLayout(new GridLayout(9,3,10,10));
		JLabel manageBookCoverUploadStatusLbl = new JLabel();
		JLabel managePlaceHolder3 = new JLabel();
		JLabel manageBookCoverUploadStatusBlank = new JLabel();
		JLabel manageBookISBNLbl = new JLabel("ISBN");
		final JTextField manageBookISBNTxt = new JTextField();
		JLabel manageBookISBNBlank = new JLabel();
		JLabel manageBookTitleLbl = new JLabel("Title");
		final JTextField manageBookTitleTxt = new JTextField();
		JLabel manageBookTitleBlank = new JLabel();
		JLabel manageBookAuthorLbl = new JLabel("Author");
		final DefaultComboBoxModel manageBookAuthorComboModel = new DefaultComboBoxModel(authorBookArray);
		final JComboBox manageBookAuthorCombo = new JComboBox(manageBookAuthorComboModel);
		final JButton manageBookAuthorBtn = new JButton("Add Author");
		JLabel manageBookYearLbl = new JLabel("Year Published");
		final JTextField manageBookYearTxt = new JTextField();
		JLabel manageBookYearBlank = new JLabel();
		JLabel manageBookGenreLbl = new JLabel("Genre");
		final DefaultComboBoxModel manageBookGenreComboModel = new DefaultComboBoxModel(genreBookArray);
		final JComboBox manageBookGenreCombo = new JComboBox(manageBookGenreComboModel);
		final JButton manageBookGenreBtn = new JButton("Add Genre");
		JLabel manageBookLengthLbl = new JLabel("Length");
		final JTextField manageBookLengthTxt = new JTextField();
		JLabel manageBookLengthBlank = new JLabel();
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
		manageAddBookEntriesPanel.add(manageBookLengthLbl);
		manageAddBookEntriesPanel.add(manageBookLengthTxt);
		manageAddBookEntriesPanel.add(manageBookLengthBlank);
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
		final DefaultComboBoxModel manageCDArtistComboModel = new DefaultComboBoxModel(authorCDArray);
		final JComboBox manageCDArtistCombo = new JComboBox(manageCDArtistComboModel);
		JLabel manageCDArtistBlank = new JLabel();
		JLabel manageCDAlbumLbl = new JLabel("Album Title");
		final JTextField manageCDAlbumTxt = new JTextField();
		JLabel manageCDAlbumBlank = new JLabel();
		JLabel manageCDGenreLbl = new JLabel("Genre");
		final DefaultComboBoxModel manageCDGenreComboModel = new DefaultComboBoxModel(genreCDArray);
		final JComboBox manageCDGenreCombo = new JComboBox(manageCDGenreComboModel);
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

		/*popup to allow new authors*/
		final JPanel addAuthorPanel = new JPanel();
		addAuthorPanel.setLayout(new GridLayout(2,2,10,10));
		final JTextField addAuthorTxt = new JTextField();
		addAuthorTxt.setPreferredSize(new Dimension(100,30));
		addAuthorTxt.setText("");
		JButton addAuthorBtn = new JButton("Add");
		final JLabel addAuthorLbl = new JLabel();
		addAuthorPanel.add(addAuthorTxt);
		addAuthorPanel.add(addAuthorBtn);
		addAuthorPanel.add(addAuthorLbl);
		addAuthorLbl.setText("Your author has been added!");
		addAuthorLbl.setVisible(false);
		
		/*popup to allow new genres*/
		final JPanel addGenrePanel = new JPanel();
		addGenrePanel.setLayout(new GridLayout(2,2,10,10));
		final JTextField addGenreTxt = new JTextField();
		addGenreTxt.setPreferredSize(new Dimension(100,30));
		addGenreTxt.setText("");
		JButton addGenreBtn = new JButton("Add");
		final JLabel addGenreLbl = new JLabel();
		addGenrePanel.add(addGenreTxt);
		addGenrePanel.add(addGenreBtn);
		addGenrePanel.add(addGenreLbl);
		addGenreLbl.setText("Your genre has been added!");
		addGenreLbl.setVisible(false);
		
		/*popup to allow new languages*/
		final JPanel addLanguagePanel = new JPanel();
		addLanguagePanel.setLayout(new GridLayout(2,2,10,10));
		final JTextField addLanguageTxt = new JTextField();
		addLanguageTxt.setPreferredSize(new Dimension(100,30));
		addLanguageTxt.setText("");
		JButton addLanguageBtn = new JButton("Add");
		final JLabel addLanguageLbl = new JLabel();
		addLanguagePanel.add(addLanguageTxt);
		addLanguagePanel.add(addLanguageBtn);
		addLanguagePanel.add(addLanguageLbl);
		addLanguageLbl.setText("Your language has been added!");
		addLanguageLbl.setVisible(false);
		
		/*popup to allow new countries*/
		final JPanel addCountryPanel = new JPanel();
		addCountryPanel.setLayout(new GridLayout(2,2,10,10));
		final JTextField addCountryTxt = new JTextField();
		addCountryTxt.setPreferredSize(new Dimension(100,30));
		addCountryTxt.setText("");
		JButton addCountryBtn = new JButton("Add");
		final JLabel addCountryLbl = new JLabel();
		addCountryPanel.add(addCountryTxt);
		addCountryPanel.add(addCountryBtn);
		addCountryPanel.add(addCountryLbl);
		addCountryLbl.setText("Your country has been added!");
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
                frame.add(autoISBNImagePopupPanel);
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
                frame.add(imageRecognitionPopupPanel);
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
                frame.add(addImportCoverImagePopupPanel);
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
                frame.add(searchImagePopupPanel);
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
                frame.add(manageBarcodeImagePopupPanel);
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
                frame.add(manageCoverSearchPopupPanel);
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
            	Icon imageIcon = movieCoverUploadStatusLbl.getIcon();
            	//turn image into blob suitable for database
            	byte[] cover = imageProcessing.getImageBlob(fileInputStream);
            	
            	
            	if(!(title.equals("") || title == null)){
	            	Movie movie = new Movie(title, director, ISBN, genre, cover, year, plot, cast, length, language, country);
	            	try {
						Database.addMovie(movie, conn);
					} catch (SQLException e) {
						e.printStackTrace();
					}
	            	addMovieStatusLbl.setText("Your movie " + title + " has been added!");
            	}
            	else{
            		JFrame parent = new JFrame();
	            	JOptionPane.showMessageDialog(parent, "Your title is blank! Please choose a title before adding a movie.", "Alert", JOptionPane.ERROR_MESSAGE);
            	}
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
            	String genre = (String) bookGenreCombo.getSelectedItem();
            	String year = bookYearTxt.getText();
            	String length = bookLengthTxt.getText();
            	//turn image into blob suitable for database
            	byte[] cover = imageProcessing.getImageBlob(fileInputStream);
            	
            	if(!(title.equals("") || title == null)){
	            	Book book = new Book(title, author, ISBN, genre, cover, year, plot, length);
	            	try {
						Database.addBook(book, conn);
					} catch (SQLException e) {
						e.printStackTrace();
					}
	            	addBookStatusLbl.setText("Your book " + title + " has been added!");
            	}
            	else{
            		JFrame parent = new JFrame();
	            	JOptionPane.showMessageDialog(parent, "Your title is blank! Please choose a title before adding a book.", "Alert", JOptionPane.ERROR_MESSAGE);
            	}
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
            	//turn image into blob suitable for database
            	byte[] cover = imageProcessing.getImageBlob(fileInputStream);
            	
            	if(!(album.equals("") || album == null)){
	            	CD cd = new CD(album, artist, ISBN, genre, cover);
	            	try {
						Database.addCD(cd, conn);
					} catch (SQLException e) {
						e.printStackTrace();
					}
	            	addCDStatusLbl.setText("Your album " + album + " has been added!");
            	}
            	else{
            		JFrame parent = new JFrame();
	            	JOptionPane.showMessageDialog(parent, "Your title is blank! Please choose a title before adding a CD.", "Alert", JOptionPane.ERROR_MESSAGE);
            	}
            }
        });		
		
		/*********************Imports an image to the add media tab*****************************/
		addImportCoverImportImageBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String picPath = "";
            	final JFileChooser fc = new JFileChooser();
            	int returnVal = fc.showOpenDialog(addImportCoverImportImageBtn);
            	if(returnVal == JFileChooser.APPROVE_OPTION){
            		File file = fc.getSelectedFile();
            		picPath = file.getPath();
            		System.out.println(picPath);
            		
            		try {
            			fileInputStream = new FileInputStream(file);
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
		
		//*********************TODO:Take photo btn for add tab import cover photo*************************
		addImportCoverTakePhotoBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
            }
        });	
		
		//*********************TODO:Import image for add tab in image recognition (OCR)*************************
		imageRecognitionImportImageBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
            }
        });	
		
		//*********************TODO:Take photo btn for add tab in image recognition*************************
		imageRecognitionTakePhotoBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
            }
        });	
		
		//*********************TODO:Automatically add image data in barcode scan*************************
		autoISBNImportImageBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
            }
        });	
		
		//*********************TODO:Take photo btn to automatically add image data in barcode scan*************************
		autoISBNTakePhotoBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
            }
        });	
		
		//*********************TODO:Import image to search the database*************************
		searchImportImageBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
            }
        });	
		
		//*********************TODO:Take photo btn to search the database by image recognition/ocr*************************
		searchTakePhotoBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
            }
        });	
		
		//*********************TODO:Import image to search by barcode to edit or delete*************************
		manageBarcodeImportImageBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
            }
        });	
		
		//*********************TODO:Take photo btn to search by barcode to edit or delete*************************
		manageBarcodeTakePhotoBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
            }
        });	
		
		//*********************TODO:Import image to search by cover photo to edit or delete*************************
		manageCoverSearchImageBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
            }
        });	
		
		//*********************TODO:Take photo btn to search by cover photo to edit or delete*************************
		manageCoverSearchTakePhotoBtn.addActionListener(new java.awt.event.ActionListener() {
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
		
		
		searchDisplayAllBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Connection conn = connection(databaseFilePath);
        		System.out.println("made it");
            	Movie[] movieArray = search.getAllMovies(conn);
            	Book[] bookArray = search.getAllBooks(conn);
            	CD[] CDArray = search.getAllCDs(conn);

            	for(int n = 0; n < movieArray.length; n++){
            		movieModel.addRow(movieArray[n].toArray());
            		movieModel.fireTableRowsInserted(movieModel.getRowCount(), movieModel.getRowCount());
            	}
            	for(int n = 0; n < bookArray.length; n++){
            		bookModel.addRow(bookArray[n].toArray());
            		bookModel.fireTableRowsInserted(bookModel.getRowCount(), bookModel.getRowCount());
            	}
            	for(int n = 0; n < CDArray.length; n++){
            		cdModel.addRow(CDArray[n].toArray());
            		cdModel.fireTableRowsInserted(cdModel.getRowCount(), cdModel.getRowCount());
            	}
            	System.out.println("made it again");
            	searchStatusLbl.setText("Display all complete");
            }
        });	
		
		
		manageEnterBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Connection conn = connection(databaseFilePath);
            	Media searchResults;
            	String ISBN = manageSearchByISBNTxt.getText();
            	searchResults = search.searchByISBN(ISBN, conn);
            	editCover = searchResults.getCover();
            	JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                
                //make sure only one panel is open at a time, bring up the edit panel for the appropriate media type
            	if(!(manageMovieEntriesPanel.isVisible() || manageAddBookEntriesPanel.isVisible() || manageAddCDEntriesPanel.isVisible())){
	                if(searchResults instanceof Movie){
	                	frame.add(manageMovieEntriesPanel);
	                	manageMovieISBNTxt.setText(searchResults.getISBN());
	                	manageMovieTitleTxt.setText(searchResults.getTitle());
	                	manageMovieDirectorCombo.setSelectedItem(searchResults.getAuthor());
	                	manageMovieGenreCombo.setSelectedItem(searchResults.getGenre());
	                	manageMovieYearTxt.setText(((Movie) searchResults).getYear());
	                	manageMovieLengthTxt.setText(((Movie) searchResults).getLength());
	                	manageMovieLanguageCombo.setSelectedItem(((Movie) searchResults).getLanguage());
	                	manageMovieCountryCombo.setSelectedItem(((Movie) searchResults).getCountry());
	                }
	                else if(searchResults instanceof Book){
	                	frame.add(manageAddBookEntriesPanel);
	                	manageBookISBNTxt.setText(searchResults.getISBN());
	                	manageBookTitleTxt.setText(searchResults.getTitle());
	                	manageBookAuthorCombo.setSelectedItem(searchResults.getAuthor());
	                	manageBookYearTxt.setText(((Book) searchResults).getYear());
	                	manageBookGenreCombo.setSelectedItem(searchResults.getGenre());
	                	manageBookLengthTxt.setText(((Book) searchResults).getLength());
	                	manageBookPlotTxt.setText(((Book) searchResults).getPlot());
	                	}
	                else if(searchResults instanceof CD){
	                	frame.add(manageAddCDEntriesPanel);
	                	manageCDISBNTxt.setText(searchResults.getISBN());
	                	manageCDArtistCombo.setSelectedItem(searchResults.getAuthor());
	                	manageCDAlbumTxt.setText(searchResults.getTitle());
	                	manageCDGenreCombo.setSelectedItem(searchResults.getGenre());
	                }
	                
	            }
            	else{
            		JLabel error = new JLabel("You already have an edit/delete page up! Please close the current before opening another.");
            		frame.add(error);
            	}
            	frame.pack();
                frame.setTitle("Edit");
                frame.setVisible(true);
            }
        });		
		
		CDGenreNewBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(addGenrePanel);
                frame.pack();
                frame.setTitle("Add Genre");
                frame.setVisible(true);
                addGenreLbl.setVisible(false);
                addGenreTxt.setText("");
                Connection conn = connection(databaseFilePath);
                Statement stat_movie;
				/*
                try {
					stat_movie = conn.createStatement();
	    			ResultSet match_movie = stat_movie.executeQuery("select * from cd");
	    			
	    			while(match_movie.next()){
	    			System.out.println(match_movie.getString());	
	    			}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
            }
        });	
		
		bookGenreBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(addGenrePanel);
                frame.pack();
                frame.setTitle("Add Genre");
                frame.setVisible(true);
                addGenreLbl.setVisible(false);
                addGenreTxt.setText("");
            }
        });	
		
		/*popup to add a genre*/
		movieGenreNewBtn.addActionListener(new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	addGenreTxt.setText("");
	        	addGenreLbl.setVisible(false);
	        	JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(addGenrePanel);
                frame.pack();
                frame.setTitle("Add Genre");
                frame.setVisible(true);
                addGenreLbl.setVisible(false);
                addGenreTxt.setText("");
	        }    
	    });	

		/*popup to add a genre database*/
		movieLanguageNewBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	addLanguageTxt.setText("");
            	addLanguageLbl.setVisible(false);
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
            	addCountryTxt.setText("");
            	addCountryLbl.setVisible(false);
            	JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(addCountryPanel);
                frame.pack();
                frame.setTitle("Add Country");
                frame.setVisible(true);
            }
        });	
				
		/*popup to add an author*/
		CDArtistBtn.addActionListener(new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	addAuthorTxt.setText("");
	        	addAuthorLbl.setVisible(false);
	        	JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(addAuthorPanel);
                frame.pack();
                frame.setTitle("Add Author");
                frame.setVisible(true);
	        }    
	    });
		
		/*popup to add an author*/
		bookAuthorBtn.addActionListener(new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	addAuthorTxt.setText("");
	        	addAuthorLbl.setVisible(false);
	        	JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(addAuthorPanel);
                frame.pack();
                frame.setTitle("Add Author");
                frame.setVisible(true);
	        }    
	    });
		
		/*popup to add an author*/
		movieDirectorBtn.addActionListener(new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	addAuthorTxt.setText("");
	        	addAuthorLbl.setVisible(false);
	        	JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(addAuthorPanel);
                frame.pack();
                frame.setTitle("Add Author");
                frame.setVisible(true);
	        }    
	    });
		
		/*adds the genre as an option to the database*/
		addGenreBtn.addActionListener(new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	            Connection conn = connection(databaseFilePath);
	        	String newGenre = addGenreTxt.getText();
		            	
	        	try{
	            	if(addMovieEntriesPanel.isVisible()){
	            		//check if value already exists
	    	            if(!search.checkForDuplicate(newGenre, new String("genre"), new String("movie"), conn)){
				            Statement stat = conn.createStatement();
				            String sql = "INSERT INTO genre (genre, media_type) VALUES('" + newGenre + "', 'movie')";
				            stat.executeUpdate(sql);
				            addGenreLbl.setVisible(true);
				            movieGenreComboModel.addElement(newGenre);
				            manageMovieGenreComboModel.addElement(newGenre);
				            
	    	            }
	    	            else{
	    	            	JFrame parent = new JFrame();
	    	            	JOptionPane.showMessageDialog(parent, "It appears this genre has already been added!", "Alert", JOptionPane.ERROR_MESSAGE);
	    	            }
	            	}
	            	else if(addBookEntriesPanel.isVisible()){
	            		//check if value already exists
	    	            if(!search.checkForDuplicate(newGenre, new String("genre"), new String("book"), conn)){
				            Statement stat = conn.createStatement();
				            String sql = "INSERT INTO genre (genre, media_type) VALUES('" + newGenre + "', 'book')";
				            stat.executeUpdate(sql);
				            addGenreLbl.setVisible(true);
				            bookGenreComboModel.addElement(newGenre);
				            manageBookGenreComboModel.addElement(newGenre);
				            
	    	            }
	    	            else{
	    	            	JFrame parent = new JFrame();
	    	            	JOptionPane.showMessageDialog(parent, "It appears this genre has already been added!", "Alert", JOptionPane.ERROR_MESSAGE);
	    	            }
	            	}
	            	else if(addCDEntriesPanel.isVisible()){
	            		//check if value already exists
	    	            if(!search.checkForDuplicate(newGenre, new String("genre"), new String("cd"), conn)){
				            Statement stat = conn.createStatement();
				            String sql = "INSERT INTO genre (genre, media_type) VALUES('" + newGenre + "', 'cd')";
				            stat.executeUpdate(sql);
				            addGenreLbl.setVisible(true);
				            CDGenreComboModel.addElement(newGenre);
				            manageCDGenreComboModel.addElement(newGenre);
				            
	    	            }
	    	            else{
	    	            	JFrame parent = new JFrame();
	    	            	JOptionPane.showMessageDialog(parent, "It appears this genre has already been added!", "Alert", JOptionPane.ERROR_MESSAGE);
	    	            }
	            	}
		            }
		            catch (SQLException e) {
		            	JFrame parent = new JFrame();
		            	JOptionPane.showMessageDialog(parent, "There was an error in entering this media!", "Alert", JOptionPane.ERROR_MESSAGE);
					}
	        }    
	    });	
		
		/*adds the author as an option to the database*/
		addAuthorBtn.addActionListener(new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	            Connection conn = connection(databaseFilePath);
	        	String newAuthor = addAuthorTxt.getText();
		            	
	        	try{
		            	if(addMovieEntriesPanel.isVisible()){
		            		//check if value already exists
		    	            if(!search.checkForDuplicate(newAuthor, new String("author"), new String("movie"), conn)){
					            Statement stat = conn.createStatement();
					            String sql = "INSERT INTO author (author, media_type) VALUES('" + newAuthor + "', 'movie')";
					            stat.executeUpdate(sql);
					            addAuthorLbl.setVisible(true);
					            movieDirectorComboModel.addElement(newAuthor);
					            manageMovieDirectorComboModel.addElement(newAuthor);
					            
		    	            }
		    	            else{
		    	            	JFrame parent = new JFrame();
		    	            	JOptionPane.showMessageDialog(parent, "It appears this author has already been added!", "Alert", JOptionPane.ERROR_MESSAGE);
		    	            }
		            	}
		            	else if(addBookEntriesPanel.isVisible()){
		            		//check if value already exists
		    	            if(!search.checkForDuplicate(newAuthor, new String("author"), new String("book"), conn)){
					            Statement stat = conn.createStatement();
					            String sql = "INSERT INTO author (author, media_type) VALUES('" + newAuthor + "', 'book')";
					            stat.executeUpdate(sql);
					            addAuthorLbl.setVisible(true);
					            bookAuthorComboModel.addElement(newAuthor);
					            manageBookAuthorComboModel.addElement(newAuthor);
					            
		    	            }
		    	            else{
		    	            	JFrame parent = new JFrame();
		    	            	JOptionPane.showMessageDialog(parent, "It appears this author has already been added!", "Alert", JOptionPane.ERROR_MESSAGE);
		    	            }
		            	}
		            	else if(addCDEntriesPanel.isVisible()){
		            		//check if value already exists
		    	            if(!search.checkForDuplicate(newAuthor, new String("author"), new String("cd"), conn)){
					            Statement stat = conn.createStatement();
					            String sql = "INSERT INTO author (author, media_type) VALUES('" + newAuthor + "', 'cd')";
					            stat.executeUpdate(sql);
					            addAuthorLbl.setVisible(true);
					            CDArtistComboModel.addElement(newAuthor);
					            manageCDArtistComboModel.addElement(newAuthor);
					            
		    	            }
		    	            else{
		    	            	JFrame parent = new JFrame();
		    	            	JOptionPane.showMessageDialog(parent, "It appears this author has already been added!", "Alert", JOptionPane.ERROR_MESSAGE);
		    	            }
		            	}
		            }
		            catch (SQLException e) {
		            	JFrame parent = new JFrame();
		            	JOptionPane.showMessageDialog(parent, "There was an error in entering this author!", "Alert", JOptionPane.ERROR_MESSAGE);
					}
	        }    
	    });	
		
		
		/*add a language to the database*/
		addLanguageBtn.addActionListener(new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	Connection conn = connection(databaseFilePath);
	        	String newLanguage = addLanguageTxt.getText();
	            Statement stat;
				
	            //check if value already exists
	            if(!search.checkForDuplicate(newLanguage, new String("language"), null, conn)){
		            try {
						stat = conn.createStatement();
						String sql = "INSERT INTO language (language) VALUES('" + newLanguage + "')";
			            stat.executeUpdate(sql);
			            addLanguageLbl.setVisible(true);
			            movieLanguageComboModel.addElement(newLanguage);
			            manageMovieLanguageComboModel.addElement(newLanguage);
			            
					} catch (SQLException e) {
						JFrame parent = new JFrame();
		            	JOptionPane.showMessageDialog(parent, "There was an error in entering this language!", "Alert", JOptionPane.ERROR_MESSAGE);
					}
	            }
	            else{
	            	JFrame parent = new JFrame();
	            	JOptionPane.showMessageDialog(parent, "It appears this language has already been added!", "Alert", JOptionPane.ERROR_MESSAGE);
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
	            
	          //check if value already exists
	            if(!search.checkForDuplicate(newCountry, new String("country"), null, conn)){
					try {
						stat = conn.createStatement();
						String sql = "INSERT INTO country (country) VALUES('" + newCountry + "')";
			            stat.executeUpdate(sql);
			            addCountryLbl.setVisible(true);
			            movieCountryComboModel.addElement(newCountry);
			            manageMovieCountryComboModel.addElement(newCountry);
			            
					} catch (SQLException e) {
						JFrame parent = new JFrame();
		            	JOptionPane.showMessageDialog(parent, "There was an error in entering this country!", "Alert", JOptionPane.ERROR_MESSAGE);
					}
	            }
	            else{
	            	JFrame parent = new JFrame();
	            	JOptionPane.showMessageDialog(parent, "It appears this country has already been added!", "Alert", JOptionPane.ERROR_MESSAGE);
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
            	Image image = (Image) manageMovieCoverUploadStatusLbl.getIcon();
            	
            	//TODO add parameters once constructors are made
            	Movie movie = new Movie(title, director, ISBN, genre, editCover, year, plot, cast, length, language, country);
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
            	Movie movie = new Movie(title, director, ISBN, genre, editCover, year, plot, cast, length, language, country);
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
            	String genre = (String) manageBookGenreCombo.getSelectedItem();
            	String year = manageBookYearTxt.getText();
            	String length = manageBookLengthTxt.getText();
            	
            	//TODO add parameters once constructors are made
            	Book book = new Book(title, author, ISBN, genre, editCover, year, plot, length);
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
            	String genre = (String) manageBookGenreCombo.getSelectedItem();
            	String year = manageBookYearTxt.getText();
            	String length = manageBookLengthTxt.getText();
            	
            	//TODO add parameters once constructors are made
            	Book book = new Book(title, author, ISBN, genre, editCover, year, plot, length);
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
            	CD cd = new CD(album, artist, ISBN, genre, editCover);
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
            	CD cd = new CD(album, artist, ISBN, genre, editCover);
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
