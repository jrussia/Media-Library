package media;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.sql.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.UIManager.*;




/*
 * CSC 478
 * Team2
 * mediaLibrary.java
 * Purpose: To store a library of movies, books, and cds 
 * with the functionality to add, search, modify, and delete.
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 1.3.8 5/6/15
 */

public class MediaLibrary extends JFrame{
	//configReader stores database and other filepaths
	static ConfigReader cr = new ConfigReader();
	public String databaseFilePath = cr.getValue("dbfilepath");
	public String imageFilePath = "";
	public InputStream fileInputStreamMovie = null;
	public InputStream fileInputStreamBook = null;
	public InputStream fileInputStreamCD = null;
	public byte[] editCover = null;
	public int currentEditID = 0;
	public BufferedImage logo;
	
	//used to connect to the sqLite database
	public static Connection connection(String filePath){
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
	
	public MediaLibrary(){
		//read image to use if no image is imported
		try {
			String iconfilepath = cr.getValue("iconfilepath");
			logo = ImageIO.read(new File(iconfilepath));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
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
		//populates the dropdown options for country, language, genre, author	
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
		JButton clearMovieBtn = new JButton("Clear");
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
		addMovieEntriesPanel2.add(clearMovieBtn);
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
		JButton clearBookBtn = new JButton("Clear");
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
		addBookEntriesPanel2.add(clearBookBtn);
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
		final JButton CDArtistBtn = new JButton("Add Artist");
		JLabel CDAlbumLbl = new JLabel("Album Title");
		final JTextField CDAlbumTxt = new JTextField();
		JLabel CDAlbumBlank = new JLabel();
		JLabel CDGenreLbl = new JLabel("Genre");
		final DefaultComboBoxModel CDGenreComboModel = new DefaultComboBoxModel(genreCDArray);
		final JComboBox CDGenreCombo = new JComboBox(CDGenreComboModel);
		JButton CDGenreNewBtn = new JButton("Add Genre");
		JButton clearCDBtn = new JButton("Clear");
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
		addCDEntriesPanel2.add(clearCDBtn);
		addCDEntriesPanel2.add(addCDBtn);
		addCDEntriesPanel2.add(addCDStatusLbl);
		addCDEntriesPanel.add(CDCoverUploadStatusLbl);
		addCDEntriesPanel.add(addCDEntriesPanel2);
		
		/*Panel for buttons to add automatically, and import cover*/
		JPanel addButtonsPanel = new JPanel();
		addButtonsPanel.setLayout(new GridLayout(3,1,10,10));
		JButton addByISBNBtn = new JButton("Automatically add by ISBN");
		JButton importCoverBtn = new JButton("Import Cover Image");
		addButtonsPanel.add(addByISBNBtn);
		addButtonsPanel.add(importCoverBtn);
		
		/*popup panel to choose how to bring an image in for import cover add tab*/
		final JPanel addImportCoverImagePopupPanel = new JPanel();
		addImportCoverImagePopupPanel.setLayout(new FlowLayout());
		final JButton addImportCoverImportImageBtn = new JButton("Import Image");
		final JButton addImportCoverTakePhotoBtn = new JButton("Take Photo");
		addImportCoverImagePopupPanel.add(addImportCoverImportImageBtn);
		addImportCoverImagePopupPanel.add(addImportCoverTakePhotoBtn);
		
		
		/*popup panel to choose how to bring an image in for auto ISBN add tab*/
		final JPanel autoISBNImagePopupPanel = new JPanel();
		autoISBNImagePopupPanel.setLayout(new GridLayout(2,3,10,10));
		final JButton autoISBNImportImageBtn = new JButton("Import Image");
		final JButton autoISBNTakePhotoBtn = new JButton("Take Photo");
		final JLabel autoISBNLbl = new JLabel("");
		final JLabel autoISBNLbl2 = new JLabel("Scan or enter your barcode manually: ");
		final JTextField autoISBNTxt = new JTextField();
		final JButton autoISBNBtn = new JButton("Enter");
		autoISBNImagePopupPanel.add(autoISBNImportImageBtn);
		autoISBNImagePopupPanel.add(autoISBNTakePhotoBtn);
		autoISBNImagePopupPanel.add(autoISBNLbl);
		autoISBNImagePopupPanel.add(autoISBNLbl2);
		autoISBNImagePopupPanel.add(autoISBNTxt);
		autoISBNImagePopupPanel.add(autoISBNBtn);
		
		/*popup panel to choose how to bring an image in for search by barcode manage tab*/
		final JPanel manageBarcodeImagePopupPanel = new JPanel();
		manageBarcodeImagePopupPanel.setLayout(new GridLayout(2,3,10,10));
		final JButton manageBarcodeImportImageBtn = new JButton("Import Image");
		final JButton manageBarcodeTakePhotoBtn = new JButton("Take Photo");
		final JLabel manageBarcodeImportLbl = new JLabel();
		final JLabel manageAutoISBNLbl = new JLabel("Scan or enter your barcode manually: ");
		final JTextField manageAutoISBNTxt = new JTextField();
		final JButton manageAutoISBNBtn = new JButton("Enter");
		manageBarcodeImagePopupPanel.add(manageBarcodeImportImageBtn);
		manageBarcodeImagePopupPanel.add(manageBarcodeTakePhotoBtn);
		manageBarcodeImagePopupPanel.add(manageBarcodeImportLbl);
		manageBarcodeImagePopupPanel.add(manageAutoISBNLbl);
		manageBarcodeImagePopupPanel.add(manageAutoISBNTxt);
		manageBarcodeImagePopupPanel.add(manageAutoISBNBtn);				
		
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
		final JTextField searchTxt = new JTextField();
		JButton searchEnterBtn = new JButton("Enter");
		JButton searchDisplayAllBtn = new JButton("Display All");
		topSearchPanel.add(searchLbl);
		topSearchPanel.add(searchTxt);
		topSearchPanel.add(searchEnterBtn);
		topSearchPanel.add(searchDisplayAllBtn);
		
		/*tables in the lookup tab*/
		JPanel searchTablesPanel = new JPanel();
		searchTablesPanel.setLayout(new BoxLayout(searchTablesPanel, BoxLayout.Y_AXIS));
		JLabel tablesMoviesLbl = new JLabel("Movies");
		String[] movieColumnNames = {"Cover", "Barcode", "Title", "Director", "Genre", "Year", "Length", "Language", "Country", "Cast", "Plot"};
		Object[][] movieTableData = {};
		//override to allow for images in the table
		final DefaultTableModel movieModel = new DefaultTableModel(){
			@Override
			public Class<?> getColumnClass(int column){
				switch(column){
				case 0: return ImageIcon.class;
				default: return String.class;
				}			
			}
			public boolean isCellEditable() {
                return false;
            }
		};
		JTable movieTable = new JTable(movieTableData, movieColumnNames);
		movieModel.setColumnIdentifiers(movieColumnNames);
		movieTable.setModel(movieModel);
		JScrollPane movieTableScrollPane = new JScrollPane(movieTable);
		movieTableScrollPane.setPreferredSize(new Dimension((movieTable.getPreferredSize()).width, (movieTable.getRowHeight()*10)));
		movieTable.setRowHeight(75);
		TableColumn columnMovie = movieTable.getColumn("Cover");
		columnMovie.setWidth(-1);
		JLabel tablesBooksLbl = new JLabel("Books");
		String[] bookColumnNames = {"Cover", "ISBN", "Title", "Author", "Genre", "Year Published", "Length", "Plot"};
		Object[][] bookTableData = {};
		//override to allow images in the table
		final DefaultTableModel bookModel = new DefaultTableModel(){
			@Override
			public Class<?> getColumnClass(int column){
				switch(column){
				case 0: return ImageIcon.class;
				default: return String.class;
				}
			}
			public boolean isCellEditable() {
                return false;
            }
		};
		JTable bookTable = new JTable(bookTableData, bookColumnNames);
		bookModel.setColumnIdentifiers(bookColumnNames);
		bookTable.setModel(bookModel);
		JScrollPane bookTableScrollPane = new JScrollPane(bookTable);
		bookTableScrollPane.setPreferredSize(new Dimension((bookTable.getPreferredSize()).width, (bookTable.getRowHeight()*10)));
		bookTable.setRowHeight(75);
		TableColumn columnBook = bookTable.getColumn("Cover");
		columnBook.setWidth(-1);
		JLabel tablesCDLbl = new JLabel("CDs");
		String[] CDColumnNames = {"Cover", "Barcode", "Artist/Band", "Album Title", "Genre"};
		Object[][] CDTableData = {};
		//override to allow images in the table
		final DefaultTableModel cdModel = new DefaultTableModel(){
			@Override
			public Class<?> getColumnClass(int column){
				switch(column){
				case 0: return ImageIcon.class;
				default: return String.class;
				}
			}
			public boolean isCellEditable() {
                return false;
            }
		};
		JTable CDTable = new JTable(CDTableData, CDColumnNames);
		final JLabel searchStatusLbl = new JLabel();
		cdModel.setColumnIdentifiers(CDColumnNames);
		CDTable.setModel(cdModel);
		JScrollPane CDTableScrollPane = new JScrollPane(CDTable);
		CDTableScrollPane.setPreferredSize(new Dimension((CDTable.getPreferredSize()).width, (CDTable.getRowHeight()*10)));
		CDTable.setRowHeight(75);
		TableColumn columnCD = CDTable.getColumn("Cover");
		columnCD.setWidth(-1);
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
		manageTopPanel.setLayout(new GridLayout(2,5,10,10));
		JLabel manageSearchByISBNLbl = new JLabel("Search by ISBN");
		final JTextField manageSearchByISBNTxt = new JTextField();
		JButton manageEnterBtn = new JButton("Enter");
		JButton manageSearchByBarcodeBtn = new JButton("Search by Barcode");
		JLabel manageSearchByTitleLbl = new JLabel("Search by Title (Exact match case sensitive)");
		final JTextField manageSearchByTitleTxt = new JTextField();
		JButton manageEnterTitleBtn = new JButton("Enter");
		//JButton manageChangeCoverBtn = new JButton("Change Cover");
		manageTopPanel.add(manageSearchByISBNLbl);
		manageTopPanel.add(manageSearchByISBNTxt);
		manageTopPanel.add(manageEnterBtn);
		manageTopPanel.add(manageSearchByBarcodeBtn);
		manageTopPanel.add(manageSearchByTitleLbl);
		manageTopPanel.add(manageSearchByTitleTxt);
		manageTopPanel.add(manageEnterTitleBtn);
		//manageTopPanel.add(manageChangeCoverBtn);
		
		/*text fields for editing or deleting movie content on manage page*/
		final JPanel manageMovieEntriesPanel = new JPanel();
		manageMovieEntriesPanel.setLayout(new GridLayout(13, 3, 10, 10));
		final JLabel space = new JLabel();
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
		JButton manageEditMovieBtn = new JButton("Update");
		JButton manageMovieDeleteBtn = new JButton("Delete");
		final JLabel movieUpdateDeleteStatusLbl = new JLabel();
		manageMovieEntriesPanel.add(space);
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
		manageMovieEntriesPanel.add(movieUpdateDeleteStatusLbl);
		
		/*text fields for editing or deleting book content on manage page*/
		final JPanel manageAddBookEntriesPanel = new JPanel();
		manageAddBookEntriesPanel.setLayout(new GridLayout(9,3,10,10));
		final JLabel space2 = new JLabel();
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
		final JLabel bookUpdateDeleteStatusLbl = new JLabel();
		manageAddBookEntriesPanel.add(space2);
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
		manageAddBookEntriesPanel.add(bookUpdateDeleteStatusLbl);
		
		/*text fields for editing or deleting CD content on manage page*/
		final JPanel manageAddCDEntriesPanel = new JPanel();
		manageAddCDEntriesPanel.setLayout(new GridLayout(8,3,10,10));
		final JLabel space3 = new JLabel();
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
		final JLabel CDUpdateDeleteStatusLbl = new JLabel();
		manageAddCDEntriesPanel.add(space3);
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
		manageAddCDEntriesPanel.add(CDUpdateDeleteStatusLbl);
		
		/*panel the user will use to update the appropriate media*/
		final JPanel editPanel = new JPanel();
		editPanel.setLayout(new FlowLayout());
		editPanel.add(manageMovieEntriesPanel);
		editPanel.add(manageAddBookEntriesPanel);
		editPanel.add(manageAddCDEntriesPanel);
		
		/*enables covers to be shown to the side of editable fields*/
		final JPanel editCoverPanel = new JPanel();
		editCoverPanel.setLayout(new FlowLayout());
		final JLabel manageCDCoverUploadStatusLbl = new JLabel();
		final JLabel manageBookCoverUploadStatusLbl = new JLabel();
		final JLabel manageMovieCoverUploadStatusLbl = new JLabel();
		editCoverPanel.add(manageCDCoverUploadStatusLbl);
		editCoverPanel.add(manageBookCoverUploadStatusLbl);
		editCoverPanel.add(manageMovieCoverUploadStatusLbl);
		
		/*all widgets for the manage Tab*/
		final JPanel managePanel = new JPanel();
		managePanel.setLayout(new BorderLayout());
		managePanel.add(manageTopPanel, BorderLayout.NORTH);
		managePanel.add(editPanel, BorderLayout.CENTER);
		managePanel.add(editCoverPanel, BorderLayout.WEST);
		manageMovieEntriesPanel.setVisible(false);
		manageAddBookEntriesPanel.setVisible(false);
		manageAddCDEntriesPanel.setVisible(false);
		
		
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
	        	if(newGenre == null){
	        		JFrame parent = new JFrame();
	            	JOptionPane.showMessageDialog(parent, "Error! You must type an entry for Genre to proceed.", "Alert", JOptionPane.ERROR_MESSAGE);
	            
	        	}
	        	else{    	
		        	try{
		            	if(addMovieEntriesPanel.isVisible()){
		            		//check if value already exists
		    	            if(!Search.checkForDuplicate(newGenre, new String("genre"), new String("movie"), conn)){
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
		    	            if(!Search.checkForDuplicate(newGenre, new String("genre"), new String("book"), conn)){
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
		    	            if(!Search.checkForDuplicate(newGenre, new String("genre"), new String("cd"), conn)){
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
	        }	
	    });	
		
		/*adds the author as an option to the database*/
		addAuthorBtn.addActionListener(new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	            Connection conn = connection(databaseFilePath);
	        	String newAuthor = addAuthorTxt.getText();
	        	if(newAuthor == null){
	        		JFrame parent = new JFrame();
	            	JOptionPane.showMessageDialog(parent, "Error! You must type an entry for Author to proceed.", "Alert", JOptionPane.ERROR_MESSAGE);
	            
	        	}
	        	else{    	
	        		try{
		            	if(addMovieEntriesPanel.isVisible()){
		            		//check if value already exists
		    	            if(!Search.checkForDuplicate(newAuthor, new String("author"), new String("movie"), conn)){
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
		    	            if(!Search.checkForDuplicate(newAuthor, new String("author"), new String("book"), conn)){
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
		    	            if(!Search.checkForDuplicate(newAuthor, new String("author"), new String("cd"), conn)){
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
	        }    
	    });	
		
		
		/*add a language to the database*/
		addLanguageBtn.addActionListener(new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	Connection conn = connection(databaseFilePath);
	        	String newLanguage = addLanguageTxt.getText();
	            Statement stat;
				
	            if(newLanguage == null){
	        		JFrame parent = new JFrame();
	            	JOptionPane.showMessageDialog(parent, "Error! You must type an entry for Language to proceed.", "Alert", JOptionPane.ERROR_MESSAGE);
	            
	        	}
	        	else{
		            //check if value already exists
		            if(!Search.checkForDuplicate(newLanguage, new String("language"), null, conn)){
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
	        }    
	    });	
		
		/*add a country to the database*/
		addCountryBtn.addActionListener(new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	Connection conn = connection(databaseFilePath);
	        	String newCountry = addCountryTxt.getText();
	            Statement stat;
	            
	            if(newCountry == null){
	        		JFrame parent = new JFrame();
	            	JOptionPane.showMessageDialog(parent, "Error! You must type an entry for Country to proceed.", "Alert", JOptionPane.ERROR_MESSAGE);
	        	}
	        	else{
		          //check if value already exists
		            if(!Search.checkForDuplicate(newCountry, new String("country"), null, conn)){
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
	        }    
	    });	
	
		/**
		 * Add a movie to the database.
		 * (Requirements 1.2.0, 1.2.1, 1.2.2, 1.2.3, 1.2.4, 1.2.5, 1.2.6,
		 * 	1.2.7, 1.2.8, 1.2.9, 1.2.10, 1.5.0, 1.3.3, 1.3.1, 1.5.0)
		 * 
		 */	
		/*Adds a movie*/
		addMovieBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JPanel confirmPanel = new JPanel();
            	confirmPanel.setLayout(new FlowLayout());
            	JLabel confirmLbl = new JLabel("Are you sure you would like to add this movie?");
            	JButton yesBtn = new JButton("Yes");
            	JButton noBtn = new JButton("No");
            	confirmPanel.add(confirmLbl);
            	confirmPanel.add(yesBtn);
            	confirmPanel.add(noBtn);
            	final JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(confirmPanel);
                frame.pack();
                frame.setTitle("Confirm Add");
                frame.setVisible(true);
				yesBtn.addActionListener(new java.awt.event.ActionListener() {
	            @Override
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	frame.setVisible(false);
	            	Connection conn = connection(databaseFilePath);
	            	String ISBN = movieISBNTxt.getText();
	            	if(ISBN == null)
	            		ISBN = "";
	            	String title = movieTitleTxt.getText();
	            	if(title == null)
	            		title = "";
	            	String director = (String) movieDirectorCombo.getSelectedItem();
	            	if(director == null)
	            		director = "";
	            	String genre = (String) movieGenreCombo.getSelectedItem();
	            	if(genre == null)
	            		genre = "";
	            	String year = movieYearTxt.getText();
	            	if(year == null)
	            		year = "";
	            	String length = movieLengthTxt.getText();
	            	if(length == null)
	            		length = "";
	            	String language = (String) movieLanguageCombo.getSelectedItem();
	            	if(language == null)
	            		language = "";
	            	String country = (String) movieCountryCombo.getSelectedItem();
	            	if(country == null)
	            		country = "";
	            	String cast = movieCastTxt.getText();
	            	if(cast == null)
	            		cast = "";
	            	String plot = moviePlotTxt.getText();
	            	if(plot == null)
	            		plot = "";
	            	Icon imageIcon = movieCoverUploadStatusLbl.getIcon();
	            	//turn image into blob suitable for database
	            	byte[] cover = ImageProcessing.getImageBlob(fileInputStreamMovie);
	            	if(cover == null){
	            		cover = new byte[255];
	            	}
	            	if((Search.searchByISBN(ISBN, conn) != null) || (Search.searchByTitle(title, conn) != null)){
	            		JFrame parent = new JFrame();
	            		if(Search.searchByISBN(ISBN, conn) != null)
	            			JOptionPane.showMessageDialog(parent, "Your barcode is a duplicate! Enter a unique barcode value to continue.", "Alert", JOptionPane.ERROR_MESSAGE);
	            		if(Search.searchByTitle(title, conn) != null){
			            	JOptionPane.showMessageDialog(parent, "Your title is a duplicate! Enter a unique title value to continue.", "Alert", JOptionPane.ERROR_MESSAGE);	
		            	}
	            	}
	            	else{
		            	if(!(title.equals("") || title == null)){
			            	
		            		Movie movie = new Movie(title, director, ISBN, genre, cover, year, plot, cast, length, language, country);
							movie = Validate.checkMovie(movie);
		            		Database.addMovie(movie);
			            	addMovieStatusLbl.setText("Your movie " + title + " has been added!");
			            	movieISBNTxt.setText("");
			            	movieDirectorCombo.setSelectedIndex(0);
			            	movieTitleTxt.setText("");
			            	movieGenreCombo.setSelectedIndex(0);
			            	movieYearTxt.setText("");
			            	movieLengthTxt.setText("");
			            	movieLanguageCombo.setSelectedIndex(0);
			            	movieCountryCombo.setSelectedIndex(0);
			            	movieCastTxt.setText("");
			            	moviePlotTxt.setText("");
			            	movieCoverUploadStatusLbl.setIcon(null);
			            	fileInputStreamMovie = null;
		            	}
		            	else{
		            		JFrame parent = new JFrame();
			            	JOptionPane.showMessageDialog(parent, "Your title is blank! Please choose a title before adding a movie.", "Alert", JOptionPane.ERROR_MESSAGE);
		            	}
	            	}
	            }
		        });	
				//else close frame
				noBtn.addActionListener(new java.awt.event.ActionListener() {
		            @Override
		            public void actionPerformed(java.awt.event.ActionEvent evt) {
		            	frame.setVisible(false);
		            }
		        });		
            }
        });
		
		clearMovieBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	movieISBNTxt.setText("");
            	movieDirectorCombo.setSelectedIndex(0);
            	movieTitleTxt.setText("");
            	movieGenreCombo.setSelectedIndex(0);
            	movieYearTxt.setText("");
            	movieLengthTxt.setText("");
            	movieLanguageCombo.setSelectedIndex(0);
            	movieCountryCombo.setSelectedIndex(0);
            	movieCastTxt.setText("");
            	moviePlotTxt.setText("");
            	movieCoverUploadStatusLbl.setIcon(null);
            	fileInputStreamMovie = null;
                
            }
        });	
		
		/**
		 * Add a book to the database.
		 * (Requirements 1.3.0, 1.3.1, 1.3.2, 1.3.3, 1.3.4, 1.3.5, 
		 * 1.3.6, 1.3.7, 1.5.0)
		 * 
		 */
		/*Adds a book*/
		addBookBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JPanel confirmPanel = new JPanel();
            	confirmPanel.setLayout(new FlowLayout());
            	JLabel confirmLbl = new JLabel("Are you sure you would like to add this book?");
            	JButton yesBtn = new JButton("Yes");
            	JButton noBtn = new JButton("No");
            	confirmPanel.add(confirmLbl);
            	confirmPanel.add(yesBtn);
            	confirmPanel.add(noBtn);
            	final JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(confirmPanel);
                frame.pack();
                frame.setTitle("Confirm Add");
                frame.setVisible(true);
				yesBtn.addActionListener(new java.awt.event.ActionListener() {
	            @Override
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	frame.setVisible(false);
	            	Connection conn = connection(databaseFilePath);
	            	String ISBN = bookISBNTxt.getText();
	            	if(ISBN == null)
	            		ISBN = "";
	            	String title = bookTitleTxt.getText();
	            	if(title == null)
	            		title = "";
	            	String author = (String) bookAuthorCombo.getSelectedItem();
	            	if(author == null)
	            		author = "";
	            	String plot = bookPlotTxt.getText();
	            	if(plot == null)
	            		plot = "";
	            	String genre = (String) bookGenreCombo.getSelectedItem();
	            	if(genre == null)
	            		genre = "";
	            	String year = bookYearTxt.getText();
	            	if(year == null)
	            		year = "";
	            	String length = bookLengthTxt.getText();
	            	if(length == null)
	            		length = "";
	            	//turn image into blob suitable for database
	            	byte[] cover = ImageProcessing.getImageBlob(fileInputStreamBook);
	            	if(cover == null){
	            		cover = new byte[255];
	            	}
	            	if((Search.searchByISBN(ISBN, conn) != null) || (Search.searchByTitle(title, conn) != null)){
	            		JFrame parent = new JFrame();
	            		if(Search.searchByISBN(ISBN, conn) != null)
	            			JOptionPane.showMessageDialog(parent, "Your ISBN is a duplicate! Enter a unique ISBN value to continue.", "Alert", JOptionPane.ERROR_MESSAGE);
	            		if(Search.searchByTitle(title, conn) != null){
			            	JOptionPane.showMessageDialog(parent, "Your title is a duplicate! Enter a unique title value to continue.", "Alert", JOptionPane.ERROR_MESSAGE);	
		            	}
	            	}
	            	else{
		            	if(!(title.equals("") || title == null)){
			            	Book book = new Book(title, author, ISBN, genre, cover, year, plot, length);
			            	book = Validate.checkBook(book);
			            	Database.addBook(book);
			            	addBookStatusLbl.setText("Your book " + title + " has been added!");
			            	bookISBNTxt.setText("");
			            	bookTitleTxt.setText("");
			            	bookAuthorCombo.setSelectedIndex(0);
			            	bookGenreCombo.setSelectedIndex(0);
			            	bookYearTxt.setText("");
			            	bookPlotTxt.setText("");
			            	bookLengthTxt.setText("");
			            	bookCoverUploadStatusLbl.setIcon(null);
			            	fileInputStreamBook = null;
		            	}
		            	else{
		            		JFrame parent = new JFrame();
			            	JOptionPane.showMessageDialog(parent, "Your title is blank! Please choose a title before adding a book.", "Alert", JOptionPane.ERROR_MESSAGE);
		            	}
	            	}
	            }
		        });	
				//else close frame
				noBtn.addActionListener(new java.awt.event.ActionListener() {
		            @Override
		            public void actionPerformed(java.awt.event.ActionEvent evt) {
		            	frame.setVisible(false);
		            }
		        });	
            }
        });
		
		clearBookBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	bookISBNTxt.setText("");
            	bookTitleTxt.setText("");
            	bookAuthorCombo.setSelectedIndex(0);
            	bookGenreCombo.setSelectedIndex(0);
            	bookYearTxt.setText("");
            	bookPlotTxt.setText("");
            	bookLengthTxt.setText("");
            	bookCoverUploadStatusLbl.setIcon(null);
            	fileInputStreamBook = null;
            }
        });	
		
		/**
		 * Add a CD to the database.
		 * (Requirements 1.4.0, 1.4.1, 1.4.2, 1.4.3, 1.4.4, 1.5.0, 1.3.3, 1.3.1, 1.5.0)
		 * 
		 */
		/*Adds a cd*/
		addCDBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JPanel confirmPanel = new JPanel();
            	confirmPanel.setLayout(new FlowLayout());
            	JLabel confirmLbl = new JLabel("Are you sure you would like to add this CD?");
            	JButton yesBtn = new JButton("Yes");
            	JButton noBtn = new JButton("No");
            	confirmPanel.add(confirmLbl);
            	confirmPanel.add(yesBtn);
            	confirmPanel.add(noBtn);
            	final JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(confirmPanel);
                frame.pack();
                frame.setTitle("Confirm Add");
                frame.setVisible(true);
				yesBtn.addActionListener(new java.awt.event.ActionListener() {
	            @Override
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	frame.setVisible(false);
	            	Connection conn = connection(databaseFilePath);
	            	
	            	String ISBN = CDISBNTxt.getText();
	            	if(ISBN == null)
	            		ISBN = "";
	            	String artist = (String) CDArtistCombo.getSelectedItem();
	            	if(artist == null)
	            		artist = "";
	            	String album = CDAlbumTxt.getText();
	            	if(album == null)
	            		album = "";
	            	String genre = (String)CDGenreCombo.getSelectedItem();
	            	if(genre == null)
	            		genre = "";
	            	//turn image into blob suitable for database
	            	byte[] cover = ImageProcessing.getImageBlob(fileInputStreamCD);
	            	if(cover == null){
	            		cover = new byte[255];
	            	}
	            	if((Search.searchByISBN(ISBN, conn) != null) || (Search.searchByTitle(album, conn) != null)){
	            		JFrame parent = new JFrame();
	            		if(Search.searchByISBN(ISBN, conn) != null)
	            			JOptionPane.showMessageDialog(parent, "Your barcode is a duplicate! Enter a unique barcode value to continue.", "Alert", JOptionPane.ERROR_MESSAGE);
	            		if(Search.searchByTitle(album, conn) != null){
			            	JOptionPane.showMessageDialog(parent, "Your title is a duplicate! Enter a unique title value to continue.", "Alert", JOptionPane.ERROR_MESSAGE);	
		            	}
	            	}
	            	else{
		            	if(!(album.equals("") || album == null)){
			            	CD cd = new CD(album, artist, ISBN, genre, cover);
			            	cd = Validate.checkCD(cd);
			            	Database.addCD(cd);
			            	addCDStatusLbl.setText("Your album " + album + " has been added!");
			            	CDISBNTxt.setText("");
			            	CDArtistCombo.setSelectedIndex(0);
			            	CDAlbumTxt.setText("");
			            	CDGenreCombo.setSelectedIndex(0);
			            	CDCoverUploadStatusLbl.setIcon(null);
			            	fileInputStreamCD = null;
		            	}
		            	else{
		            		JFrame parent = new JFrame();
			            	JOptionPane.showMessageDialog(parent, "Your album is blank! Please choose an album before adding a CD.", "Alert", JOptionPane.ERROR_MESSAGE);
		            	}
	            	}
	            }
		        });	
				//else close frame
				noBtn.addActionListener(new java.awt.event.ActionListener() {
		            @Override
		            public void actionPerformed(java.awt.event.ActionEvent evt) {
		            	frame.setVisible(false);
		            }
		        });	
            }
        });	
		
		clearCDBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	CDISBNTxt.setText("");
            	CDArtistCombo.setSelectedIndex(0);
            	CDAlbumTxt.setText("");
            	CDGenreCombo.setSelectedIndex(0);
            	CDCoverUploadStatusLbl.setIcon(null);
            	fileInputStreamCD = null;
            }
        });	
		
		/**
		 * Import cover
		 * (Requirements 1.3.0, 1.3.7, 1.4.3)
		 * 
		 */
		/*Imports an image to the add media tab*/
		addImportCoverImportImageBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String picPath = "";
            	final JFileChooser fc = new JFileChooser();
            	int returnVal = fc.showOpenDialog(addImportCoverImportImageBtn);
            	if(returnVal == JFileChooser.APPROVE_OPTION){
            		File file = fc.getSelectedFile();
            		picPath = file.getPath();
            		
            		try {
						if(addPanel.isVisible()){
							if(addMovieEntriesPanel.isVisible()){
								fileInputStreamMovie = new FileInputStream(file);
		            			Image img = ImageIO.read(fc.getSelectedFile());
								Image resizedImage = img.getScaledInstance(150, -1, Image.SCALE_SMOOTH);
								ImageIcon chosenPicture = new ImageIcon(file.getPath());
								movieCoverUploadStatusLbl.setIcon(new ImageIcon(resizedImage));
								addMovieEntriesPanel.validate();
							}
							else if(addBookEntriesPanel.isVisible()){
								fileInputStreamBook = new FileInputStream(file);
		            			Image img = ImageIO.read(fc.getSelectedFile());
								Image resizedImage = img.getScaledInstance(150, -1, Image.SCALE_SMOOTH);
								ImageIcon chosenPicture = new ImageIcon(file.getPath());
								bookCoverUploadStatusLbl.setIcon(new ImageIcon(resizedImage));
								addBookEntriesPanel.validate();
							}
							else if(addCDEntriesPanel.isVisible()){
								fileInputStreamCD = new FileInputStream(file);
		            			Image img = ImageIO.read(fc.getSelectedFile());
								Image resizedImage = img.getScaledInstance(150, -1, Image.SCALE_SMOOTH);
								ImageIcon chosenPicture = new ImageIcon(file.getPath());
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
						
					} catch (IOException e) {
						JFrame parent = new JFrame();
		            	JOptionPane.showMessageDialog(parent, "Your image was unable to be added. Ensure the file selected was an image type. It is possible the image is too large and must be compressed.", "Alert", JOptionPane.ERROR_MESSAGE);
					}
            		
            	}
            }
        });		
		
		/**
		 * Take Photo to Import cover
		 * (Requirements 1.3.0, 1.3.7, 1.4.3, 1.5.0)
		 * 
		 */
		//*********************Take photo btn for add tab import cover photo*************************
		addImportCoverTakePhotoBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TakePicture.showWebcam();
           		
                (TakePicture.captureBtn).addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                    	final BufferedImage image = (TakePicture.webcam).getImage();     
                    	if(image != null){
                    		TakePicture.img = image;
                    		
                    		JLabel picLbl = new JLabel(new ImageIcon(TakePicture.img));
                    		JButton acceptBtn = new JButton("Accept");
                    		JButton retakeBtn = new JButton("Retake");
                    		JPanel buttonsPanel = new JPanel();
                    		buttonsPanel.setLayout(new FlowLayout());
                    		buttonsPanel.add(acceptBtn);
                    		buttonsPanel.add(retakeBtn);
                    		JPanel picTakenPanel = new JPanel();
                    		picTakenPanel.setLayout(new BorderLayout());
                    		picTakenPanel.add(picLbl, BorderLayout.CENTER);
                    		picTakenPanel.add(buttonsPanel, BorderLayout.SOUTH);
                    		
                            final JFrame frame2 = new JFrame();
                            frame2.setLayout(new FlowLayout());
                            frame2.add(picTakenPanel);
                            frame2.pack();
                            frame2.setTitle("Picture");
                            frame2.setVisible(true);
                    		acceptBtn.addActionListener(new java.awt.event.ActionListener() {
                                @Override
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                	if(addPanel.isVisible()){
                                		Image resizedImage = (TakePicture.img).getScaledInstance(150, -1, Image.SCALE_SMOOTH);
            							
                                		if(addMovieEntriesPanel.isVisible()){
            								movieCoverUploadStatusLbl.setIcon(new ImageIcon(resizedImage));
            								addMovieEntriesPanel.validate();
            								ByteArrayOutputStream os = new ByteArrayOutputStream();
            								try {
												ImageIO.write(image, "png", os);
												fileInputStreamMovie = new ByteArrayInputStream(os.toByteArray());
											} catch (IOException e) {
												JFrame parent = new JFrame();
								            	JOptionPane.showMessageDialog(parent, "Your image was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
												e.printStackTrace();
											}
            							}
            							else if(addBookEntriesPanel.isVisible()){
            								bookCoverUploadStatusLbl.setIcon(new ImageIcon(resizedImage));
            								addBookEntriesPanel.validate();
            								ByteArrayOutputStream os = new ByteArrayOutputStream();
            								try {
												ImageIO.write(image, "png", os);
												fileInputStreamBook = new ByteArrayInputStream(os.toByteArray());
											} catch (IOException e) {
												JFrame parent = new JFrame();
								            	JOptionPane.showMessageDialog(parent, "Your image was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
												
												e.printStackTrace();
											}
            							}
            							else if(addCDEntriesPanel.isVisible()){
            								CDCoverUploadStatusLbl.setIcon(new ImageIcon(resizedImage));
            								addCDEntriesPanel.validate();
            								
            								ByteArrayOutputStream os = new ByteArrayOutputStream();
            								try {
												ImageIO.write(image, "png", os);
												fileInputStreamCD = new ByteArrayInputStream(os.toByteArray());
											} catch (IOException e) {
												JFrame parent = new JFrame();
								            	JOptionPane.showMessageDialog(parent, "Your image was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
												e.printStackTrace();
											}
            								
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
                                	(TakePicture.frame).dispatchEvent(new WindowEvent(TakePicture.frame, WindowEvent.WINDOW_CLOSING));
                                	(frame2).dispatchEvent(new WindowEvent(frame2, WindowEvent.WINDOW_CLOSING));
                                	(TakePicture.webcam).close();
                                }
                    		});  
                    		retakeBtn.addActionListener(new java.awt.event.ActionListener() {
                                @Override
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                	frame2.setVisible(false);
                                	TakePicture.showWebcam();
                                }
                    		});
                    	}
                    	else{
                    		JFrame parent = new JFrame();
                        	JOptionPane.showMessageDialog(parent, "Your picture was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
                    	}
                    }
                });	
            }
        });	
		
		/**
		 * Automatically bring in data by ISBN
		 * (Requirements 1.3.0, 1.3.1, 1.3.2, 1.3.3, 1.3.4, 1.3.5, 1.3.6, 1.3.7, 
		 * 1.2.0, 1.2.1, 1.2.2, 1.2.3, 1.2.4, 1.2.5, 1.2.6, 1.2.7, 1.2.8, 1.2.9, 
		 * 1.2.10, 1.4.0, 1.4.1, 1.4.2, 1.4.3, 1.4.4, 4.1.0, 4.1.1, 1.1.2, 1.5.0)
		 * 
		 */
		//*********************Automatically add image data in barcode scan*************************
		autoISBNBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	movieISBNTxt.setText("");
            	movieDirectorCombo.setSelectedIndex(0);
            	movieTitleTxt.setText("");
            	movieGenreCombo.setSelectedIndex(0);
            	movieYearTxt.setText("");
            	movieLengthTxt.setText("");
            	movieLanguageCombo.setSelectedIndex(0);
            	movieCountryCombo.setSelectedIndex(0);
            	movieCastTxt.setText("");
            	moviePlotTxt.setText("");
            	movieCoverUploadStatusLbl.setIcon(null);
            	
            	bookISBNTxt.setText("");
            	bookTitleTxt.setText("");
            	bookAuthorCombo.setSelectedIndex(0);
            	bookGenreCombo.setSelectedIndex(0);
            	bookYearTxt.setText("");
            	bookPlotTxt.setText("");
            	bookLengthTxt.setText("");
            	bookCoverUploadStatusLbl.setIcon(null);
            	
            	CDISBNTxt.setText("");
            	CDArtistCombo.setSelectedIndex(0);
            	CDAlbumTxt.setText("");
            	CDGenreCombo.setSelectedIndex(0);
            	CDCoverUploadStatusLbl.setIcon(null);
            	
            	autoISBNLbl.setText("");
            	String barcode = autoISBNTxt.getText();
            	if(barcode == null || barcode.equals("")){
            		JFrame parent = new JFrame();
	            	JOptionPane.showMessageDialog(parent, "Invalid Entry!", "Alert", JOptionPane.ERROR_MESSAGE);
            	}
            	else{
	            	if(addMovieEntriesPanel.isVisible()){
	                	try {
							final Movie[] movies = InternetLookup.lookupMovie(barcode);
							if(movies.length == 0){
								autoISBNLbl.setText("No matches were found");
							}
							else{
								JPanel panel = new JPanel();
								panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
								
								for(int n = 0; n < movies.length; n++){
									panel.add(new JLabel((n + 1) + ("\n") + movies[n].toString() + ("\n")));
								}
								final JTextField movieSelectionTxt = new JTextField();
								panel.add(movieSelectionTxt);
								JButton movieSelectionBtn = new JButton("Choose (1-" + movies.length + ")");
								panel.add(movieSelectionBtn);
								JFrame frame2 = new JFrame();
	                            frame2.setLayout(new FlowLayout());
	                            frame2.add(panel);
	                            frame2.pack();
	                            frame2.setTitle("Picture");
	                            frame2.setVisible(true);
								/*Choose which movie option to automatically enter*/
								movieSelectionBtn.addActionListener(new java.awt.event.ActionListener() {
						            @Override
						            public void actionPerformed(java.awt.event.ActionEvent evt) {
						                if((Integer.parseInt(movieSelectionTxt.getText()) > 0) && (Integer.parseInt(movieSelectionTxt.getText()) <= movies.length)){
						                	int selection = Integer.parseInt(movieSelectionTxt.getText());
						                	Movie movie = movies[(selection - 1)];
						                	movieISBNTxt.setText(movie.getISBN());
						                	movieTitleTxt.setText(movie.getTitle());
						                	movieDirectorCombo.setSelectedItem(movie.getAuthor());
						                	movieGenreCombo.setSelectedItem(movie.getGenre());
						                	movieYearTxt.setText(movie.getYear());
						                	movieLengthTxt.setText(movie.getLength());
						                	movieLanguageCombo.setSelectedItem(movie.getLanguage());
						                	movieCountryCombo.setSelectedItem(movie.getCountry());
						                	movieCastTxt.setText(movie.getCast());
						                	moviePlotTxt.setText(movie.getPlot());
						                }
						                else{
						                	JFrame parent = new JFrame();
							            	JOptionPane.showMessageDialog(parent, "Your entry was not one of the options given!", "Alert", JOptionPane.ERROR_MESSAGE);
											
						                }
						            }
						        });	
							}
						} catch (Exception e) {
							JFrame parent = new JFrame();
			            	JOptionPane.showMessageDialog(parent, "Your barcode was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						}
	                	
	                }
	                else if(addBookEntriesPanel.isVisible()){
	                	try {
							Book book = InternetLookup.lookupBook(barcode);
							if(book == null){
								autoISBNLbl.setText("No matches were found");
							}
							else{
								bookISBNTxt.setText(book.getISBN());
								bookTitleTxt.setText(book.getTitle());
								bookAuthorCombo.setSelectedItem(book.getAuthor());
								bookLengthTxt.setText(book.getLength());
								bookYearTxt.setText(book.getYear());
								bookGenreCombo.setSelectedItem(book.getGenre());
								bookPlotTxt.setText(book.getPlot());
							}
						} catch (Exception e) {
							JFrame parent = new JFrame();
			            	JOptionPane.showMessageDialog(parent, "Your barcode was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						}
	                }
	                else if(addCDEntriesPanel.isVisible()){
	                	try {
							CD cd = InternetLookup.lookupCD(barcode);
							if(cd == null){
								autoISBNLbl.setText("No matches were found");
							}
							else{
								CDISBNTxt.setText(cd.getISBN());
								CDArtistCombo.setSelectedItem(cd.getAuthor());
								CDAlbumTxt.setText(cd.getTitle());
								CDGenreCombo.setSelectedItem(cd.getGenre());
							}
						} catch (Exception e) {
							JFrame parent = new JFrame();
			            	JOptionPane.showMessageDialog(parent, "Your barcode was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						}
	                }
	                else{
	                	JFrame parent = new JFrame();
		            	JOptionPane.showMessageDialog(parent, "Error, select a media type first!", "Alert", JOptionPane.ERROR_MESSAGE);
	                }
            	}
            	autoISBNTxt.setText("");
            	
            }
        });	
		
		/**
		 * Automatically bring in data by ISBN by importing an image
		 * (Requirements 1.3.0, 1.3.1, 1.3.2, 1.3.3, 1.3.4, 1.3.5, 1.3.6, 1.3.7, 
		 * 1.2.0, 1.2.1, 1.2.2, 1.2.3, 1.2.4, 1.2.5, 1.2.6, 1.2.7, 1.2.8, 1.2.9, 
		 * 1.2.10, 1.4.0, 1.4.1, 1.4.2, 1.4.3, 1.4.4, 4.1.0, 4.1.1, 1.1.2, 1.5.0)
		 * 
		 */
		//*********************Automatically add image data in barcode scan*************************
		autoISBNImportImageBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	movieISBNTxt.setText("");
            	movieDirectorCombo.setSelectedIndex(0);
            	movieTitleTxt.setText("");
            	movieGenreCombo.setSelectedIndex(0);
            	movieYearTxt.setText("");
            	movieLengthTxt.setText("");
            	movieLanguageCombo.setSelectedIndex(0);
            	movieCountryCombo.setSelectedIndex(0);
            	movieCastTxt.setText("");
            	moviePlotTxt.setText("");
            	movieCoverUploadStatusLbl.setIcon(null);
            	
            	bookISBNTxt.setText("");
            	bookTitleTxt.setText("");
            	bookAuthorCombo.setSelectedIndex(0);
            	bookGenreCombo.setSelectedIndex(0);
            	bookYearTxt.setText("");
            	bookPlotTxt.setText("");
            	bookLengthTxt.setText("");
            	bookCoverUploadStatusLbl.setIcon(null);
            	
            	CDISBNTxt.setText("");
            	CDArtistCombo.setSelectedIndex(0);
            	CDAlbumTxt.setText("");
            	CDGenreCombo.setSelectedIndex(0);
            	CDCoverUploadStatusLbl.setIcon(null);
            	
            	String picPath = "";
            	autoISBNLbl.setText("");
            	final JFileChooser fc = new JFileChooser();
            	int returnVal = fc.showOpenDialog(addImportCoverImportImageBtn);
            	if(returnVal == JFileChooser.APPROVE_OPTION){
            		File file = fc.getSelectedFile();
            		picPath = file.getPath();
            	}
            	InputStream barcodeIS;
            	String barcode = "";
				try {
					barcodeIS = new FileInputStream(picPath);
	            	barcode = ImageProcessing.readBarcode(barcodeIS);
				} catch (FileNotFoundException e1) {
					JFrame parent = new JFrame();
	            	JOptionPane.showMessageDialog(parent, "Your barcode was unable to process! It's possible your image was not clear enough.", "Alert", JOptionPane.ERROR_MESSAGE);
					
					e1.printStackTrace();
				}
            	
            	if(addMovieEntriesPanel.isVisible()){
                	try {
						final Movie[] movies = InternetLookup.lookupMovie(barcode);
						if(movies.length == 0){
							autoISBNLbl.setText("No matches were found");
						}
						else{
							JPanel panel = new JPanel();
							panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
							for(int n = 0; n < movies.length; n++){
								panel.add(new JLabel((n + 1) + ("\n") + movies[n].toString() + ("\n")));
							}
							final JTextField movieSelectionTxt = new JTextField();
							panel.add(movieSelectionTxt);
							JButton movieSelectionBtn = new JButton("Choose (1-" + movies.length + ")");
							panel.add(movieSelectionBtn);
							JFrame frame2 = new JFrame();
                            frame2.setLayout(new FlowLayout());
                            frame2.add(panel);
                            frame2.pack();
                            frame2.setTitle("Picture");
                            frame2.setVisible(true);
							/*Choose which movie option to automatically enter*/
							movieSelectionBtn.addActionListener(new java.awt.event.ActionListener() {
					            @Override
					            public void actionPerformed(java.awt.event.ActionEvent evt) {
					                if((Integer.parseInt(movieSelectionTxt.getText()) > 0) && (Integer.parseInt(movieSelectionTxt.getText()) <= movies.length)){
					                	int selection = Integer.parseInt(movieSelectionTxt.getText());
					                	Movie movie = movies[(selection - 1)];
					                	movieISBNTxt.setText(movie.getISBN());
					                	movieTitleTxt.setText(movie.getTitle());
					                	movieDirectorCombo.setSelectedItem(movie.getAuthor());
					                	movieGenreCombo.setSelectedItem(movie.getGenre());
					                	movieYearTxt.setText(movie.getYear());
					                	movieLengthTxt.setText(movie.getLength());
					                	movieLanguageCombo.setSelectedItem(movie.getLanguage());
					                	movieCountryCombo.setSelectedItem(movie.getCountry());
					                	movieCastTxt.setText(movie.getCast());
					                	moviePlotTxt.setText(movie.getPlot());
					                }
					                else{
					                	JFrame parent = new JFrame();
						            	JOptionPane.showMessageDialog(parent, "Your entry was not one of the options given!", "Alert", JOptionPane.ERROR_MESSAGE);
										
					                }
					            }
					        });	
						}
					} catch (Exception e) {
						JFrame parent = new JFrame();
		            	JOptionPane.showMessageDialog(parent, "Your barcode was unable to process! It's possible your barcode was not clear enough.", "Alert", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
                	
                }
                else if(addBookEntriesPanel.isVisible()){
                	try {
						Book book = InternetLookup.lookupBook(barcode);
						if(book == null){
							autoISBNLbl.setText("No matches were found");
						}
						else{
							bookISBNTxt.setText(book.getISBN());
							bookTitleTxt.setText(book.getAuthor());
							bookAuthorCombo.setSelectedItem(book.getAuthor());
							bookLengthTxt.setText(book.getLength());
							bookYearTxt.setText(book.getYear());
							bookGenreCombo.setSelectedItem(book.getGenre());
							bookPlotTxt.setText(book.getPlot());
						}
					} catch (Exception e) {
						JFrame parent = new JFrame();
		            	JOptionPane.showMessageDialog(parent, "Your barcode was unable to process! It's possible your image was not clear enough.", "Alert", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
                }
                else if(addCDEntriesPanel.isVisible()){
                	try {
						CD cd = InternetLookup.lookupCD(barcode);
						if(cd == null){
							autoISBNLbl.setText("No matches were found");
						}
						else{
							CDISBNTxt.setText(cd.getISBN());
							CDArtistCombo.setSelectedItem(cd.getAuthor());
							CDAlbumTxt.setText(cd.getTitle());
							CDGenreCombo.setSelectedItem(cd.getGenre());
						}
					} catch (Exception e) {
						JFrame parent = new JFrame();
		            	JOptionPane.showMessageDialog(parent, "Your barcode was unable to process! It's possible your image was not clear enough", "Alert", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
                }
                else{
                	JFrame parent = new JFrame();
	            	JOptionPane.showMessageDialog(parent, "Error, select a media type first!", "Alert", JOptionPane.ERROR_MESSAGE);
                }
            }
        });	
			
		/**
		 * Automatically bring in data by ISBN by taking a photo and reading the barcode
		 * (Requirements 1.3.0, 1.3.1, 1.3.2, 1.3.3, 1.3.4, 1.3.5, 1.3.6, 1.3.7, 
		 * 1.2.0, 1.2.1, 1.2.2, 1.2.3, 1.2.4, 1.2.5, 1.2.6, 1.2.7, 1.2.8, 1.2.9, 
		 * 1.2.10, 1.4.0, 1.4.1, 1.4.2, 1.4.3, 1.4.4, 4.1.0, 4.1.1, 1.1.2, 1.5.0)
		 * 
		 */
		//*********************Take photo btn to automatically add data in barcode scan*************************
		autoISBNTakePhotoBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	movieISBNTxt.setText("");
            	movieDirectorCombo.setSelectedIndex(0);
            	movieTitleTxt.setText("");
            	movieGenreCombo.setSelectedIndex(0);
            	movieYearTxt.setText("");
            	movieLengthTxt.setText("");
            	movieLanguageCombo.setSelectedIndex(0);
            	movieCountryCombo.setSelectedIndex(0);
            	movieCastTxt.setText("");
            	moviePlotTxt.setText("");
            	movieCoverUploadStatusLbl.setIcon(null);
            	
            	bookISBNTxt.setText("");
            	bookTitleTxt.setText("");
            	bookAuthorCombo.setSelectedIndex(0);
            	bookGenreCombo.setSelectedIndex(0);
            	bookYearTxt.setText("");
            	bookPlotTxt.setText("");
            	bookLengthTxt.setText("");
            	bookCoverUploadStatusLbl.setIcon(null);
            	
            	CDISBNTxt.setText("");
            	CDArtistCombo.setSelectedIndex(0);
            	CDAlbumTxt.setText("");
            	CDGenreCombo.setSelectedIndex(0);
            	CDCoverUploadStatusLbl.setIcon(null);
            	
            	
                TakePicture.showWebcam();
           		
                (TakePicture.captureBtn).addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                    	final BufferedImage image = (TakePicture.webcam).getImage();     
                    	if(image != null){
                    		TakePicture.img = image;
                    		
                    		JLabel picLbl = new JLabel(new ImageIcon(TakePicture.img));
                    		JButton acceptBtn = new JButton("Accept");
                    		JButton retakeBtn = new JButton("Retake");
                    		JPanel buttonsPanel = new JPanel();
                    		buttonsPanel.setLayout(new FlowLayout());
                    		buttonsPanel.add(acceptBtn);
                    		buttonsPanel.add(retakeBtn);
                    		JPanel picTakenPanel = new JPanel();
                    		picTakenPanel.setLayout(new BorderLayout());
                    		picTakenPanel.add(picLbl, BorderLayout.CENTER);
                    		picTakenPanel.add(buttonsPanel, BorderLayout.SOUTH);
                    		
                            final JFrame frame2 = new JFrame();
                            frame2.setLayout(new FlowLayout());
                            frame2.add(picTakenPanel);
                            frame2.pack();
                            frame2.setTitle("Picture");
                            frame2.setVisible(true);
                    		acceptBtn.addActionListener(new java.awt.event.ActionListener() {
                                @Override
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                	Image resizedImage = (TakePicture.img).getScaledInstance(150, -1, Image.SCALE_SMOOTH);
                                	ByteArrayOutputStream os = new ByteArrayOutputStream();
                                	
                                	InputStream barcodeIS;
                                	String barcode = "";
                    				try {
                    					ImageIO.write(image, "png", os);
										barcodeIS = new ByteArrayInputStream(os.toByteArray());
                    	            	barcode = ImageProcessing.readBarcode(barcodeIS);
                    				} catch (FileNotFoundException e1) {
                    					JFrame parent = new JFrame();
                    	            	JOptionPane.showMessageDialog(parent, "Your file was not found!", "Alert", JOptionPane.ERROR_MESSAGE);
                    					
                    					e1.printStackTrace();
                    				} catch (Exception e) {
                    					JFrame parent = new JFrame();
                    	            	JOptionPane.showMessageDialog(parent, "Your barcode was unable to process! It's possible your image was not clear enough.", "Alert", JOptionPane.ERROR_MESSAGE);
                    					e.printStackTrace();
									}
                                	
                                	if(addMovieEntriesPanel.isVisible()){
                                    	try {
                    						final Movie[] movies = InternetLookup.lookupMovie(barcode);
                    						if(movies.length == 0){
                    							autoISBNLbl.setText("No matches were found");
                    						}
                    						else{
                    							JPanel panel = new JPanel();
                								panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                    							for(int n = 0; n < movies.length; n++){
                    								panel.add(new JLabel((n + 1) + ("\n") + movies[n].toString() + ("\n")));
                    							}
                    							final JTextField movieSelectionTxt = new JTextField();
                    							panel.add(movieSelectionTxt);
                    							JButton movieSelectionBtn = new JButton("Choose (1-" + movies.length + ")");
                    							panel.add(movieSelectionBtn);
                    							JFrame frame2 = new JFrame();
                	                            frame2.setLayout(new FlowLayout());
                	                            frame2.add(panel);
                	                            frame2.pack();
                	                            frame2.setTitle("Picture");
                	                            frame2.setVisible(true);
                    							/*Choose which movie option to automatically enter*/
                    							movieSelectionBtn.addActionListener(new java.awt.event.ActionListener() {
                    					            @Override
                    					            public void actionPerformed(java.awt.event.ActionEvent evt) {
                    					                if((Integer.parseInt(movieSelectionTxt.getText()) > 0) && (Integer.parseInt(movieSelectionTxt.getText()) <= movies.length)){
                    					                	int selection = Integer.parseInt(movieSelectionTxt.getText());
                    					                	Movie movie = movies[(selection - 1)];
                    					                	movieISBNTxt.setText(movie.getISBN());
                    					                	movieTitleTxt.setText(movie.getTitle());
                    					                	movieDirectorCombo.setSelectedItem(movie.getAuthor());
                    					                	movieGenreCombo.setSelectedItem(movie.getGenre());
                    					                	movieYearTxt.setText(movie.getYear());
                    					                	movieLengthTxt.setText(movie.getLength());
                    					                	movieLanguageCombo.setSelectedItem(movie.getLanguage());
                    					                	movieCountryCombo.setSelectedItem(movie.getCountry());
                    					                	movieCastTxt.setText(movie.getCast());
                    					                	moviePlotTxt.setText(movie.getPlot());
                    					                }
                    					                else{
                    					                	JFrame parent = new JFrame();
                    						            	JOptionPane.showMessageDialog(parent, "Your entry was not one of the options given!", "Alert", JOptionPane.ERROR_MESSAGE);
                    										
                    					                }
                    					            }
                    					        });	
                    						}
                    					} catch (Exception e) {
                    						JFrame parent = new JFrame();
                    		            	JOptionPane.showMessageDialog(parent, "Your barcode was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
                    						e.printStackTrace();
                    					}
                                    	
                                    }
                                    else if(addBookEntriesPanel.isVisible()){
                                    	try {
                    						Book book = InternetLookup.lookupBook(barcode);
                    						if(book == null){
                    							autoISBNLbl.setText("No matches were found");
                    						}
                    						else{
                    							bookISBNTxt.setText(book.getISBN());
                    							bookTitleTxt.setText(book.getAuthor());
                    							bookAuthorCombo.setSelectedItem(book.getAuthor());
                    							bookLengthTxt.setText(book.getLength());
                    							bookYearTxt.setText(book.getYear());
                    							bookGenreCombo.setSelectedItem(book.getGenre());
                    							bookPlotTxt.setText(book.getPlot());
                    						}
                    					} catch (Exception e) {
                    						JFrame parent = new JFrame();
                    		            	JOptionPane.showMessageDialog(parent, "Your barcode was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
                    						e.printStackTrace();
                    					}
                                    }
                                    else if(addCDEntriesPanel.isVisible()){
                                    	try {
                    						CD cd = InternetLookup.lookupCD(barcode);
                    						if(cd == null){
                    							autoISBNLbl.setText("No matches were found");
                    						}
                    						else{
                    							CDISBNTxt.setText(cd.getISBN());
                    							CDArtistCombo.setSelectedItem(cd.getAuthor());
                    							CDAlbumTxt.setText(cd.getTitle());
                    							CDGenreCombo.setSelectedItem(cd.getGenre());
                    						}
                    					} catch (Exception e) {
                    						JFrame parent = new JFrame();
                    		            	JOptionPane.showMessageDialog(parent, "Your barcode was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
                    						e.printStackTrace();
                    					}
                                    }
                                    else{
                                    	JFrame parent = new JFrame();
                    	            	JOptionPane.showMessageDialog(parent, "Error, select a media type first!", "Alert", JOptionPane.ERROR_MESSAGE);
                                    }
                                	
                                	(TakePicture.frame).dispatchEvent(new WindowEvent(TakePicture.frame, WindowEvent.WINDOW_CLOSING));
                                	(frame2).dispatchEvent(new WindowEvent(frame2, WindowEvent.WINDOW_CLOSING));
                                	(TakePicture.webcam).close();
                                }
                    		});  
                    		retakeBtn.addActionListener(new java.awt.event.ActionListener() {
                                @Override
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                	frame2.setVisible(false);
                                	TakePicture.showWebcam();
                                }
                    		});
                    	}
                    	else{
                    		JFrame parent = new JFrame();
                        	JOptionPane.showMessageDialog(parent, "Your picture was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
                    	}
                    }
                });	
            }	
        });	
		
		/**
		 * Automatically bring in data by ISBN from already entered data
		 * by importing a photo and reading the barcode so the user can edit
		 * (Requirements 1.1.2, 1.5.0, 2.0.0, 2.1.0, 2.2.0, 2.3.0, 3.0.0, 3.1.0, 3.2.0)
		 * 
		 */
		//********************Import image to search by barcode to edit or delete**************************
		manageBarcodeImportImageBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Connection conn = connection(databaseFilePath);
            	manageBarcodeImportLbl.setText("");
            	String picPath = "";
            	final JFileChooser fc = new JFileChooser();
            	int returnVal = fc.showOpenDialog(addImportCoverImportImageBtn);
            	if(returnVal == JFileChooser.APPROVE_OPTION){
            		File file = fc.getSelectedFile();
            		picPath = file.getPath();
            	}
            	try{
            		InputStream barcodeIS = new FileInputStream(picPath);
            		String barcode = ImageProcessing.readBarcode(barcodeIS);
                	Media media = Search.searchByISBN(barcode, conn);
                	if(media == null){
                		JFrame parent = new JFrame();
    	            	JOptionPane.showMessageDialog(parent, "Your ISBN did not have a match in the database!", "Alert", JOptionPane.ERROR_MESSAGE);
                    
                	}
                	else{
                	if(media instanceof Movie){
                    	try {
    						final Movie[] movies = InternetLookup.lookupMovie(barcode);
    						if(movies.length == 0){
    							autoISBNLbl.setText("No matches were found");
    						}
    						else{
    							JPanel panel = new JPanel();
								panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    							for(int n = 0; n < movies.length; n++){
    								panel.add(new JLabel((n + 1) + ("\n") + movies[n].toString() + ("\n")));
    							}
    							final JTextField movieSelectionTxt = new JTextField();
    							panel.add(movieSelectionTxt);
    							JButton movieSelectionBtn = new JButton("Choose (1-" + movies.length + ")");
    							panel.add(movieSelectionBtn);
    							JFrame frame2 = new JFrame();
	                            frame2.setLayout(new FlowLayout());
	                            frame2.add(panel);
	                            frame2.pack();
	                            frame2.setTitle("Picture");
	                            frame2.setVisible(true);
    							/*Choose which movie option to automatically enter*/
    							movieSelectionBtn.addActionListener(new java.awt.event.ActionListener() {
    					            @Override
    					            public void actionPerformed(java.awt.event.ActionEvent evt) {
    					                if((Integer.parseInt(movieSelectionTxt.getText()) > 0) && (Integer.parseInt(movieSelectionTxt.getText()) <= movies.length)){
    					                	int selection = Integer.parseInt(movieSelectionTxt.getText());
    					                	Movie movie = movies[(selection - 1)];
    					                	manageMovieISBNTxt.setText(movie.getISBN());
    					                	manageMovieTitleTxt.setText(movie.getTitle());
    					                	manageMovieDirectorCombo.setSelectedItem(movie.getAuthor());
    					                	manageMovieGenreCombo.setSelectedItem(movie.getGenre());
    					                	manageMovieYearTxt.setText(movie.getYear());
    					                	manageMovieLengthTxt.setText(movie.getLength());
    					                	manageMovieLanguageCombo.setSelectedItem(movie.getLanguage());
    					                	manageMovieCountryCombo.setSelectedItem(movie.getCountry());
    					                	manageMovieCastTxt.setText(movie.getCast());
    					                	manageMoviePlotTxt.setText(movie.getPlot());
    					                }
    					                else{
    					                	JFrame parent = new JFrame();
    						            	JOptionPane.showMessageDialog(parent, "Your entry was not one of the options given!", "Alert", JOptionPane.ERROR_MESSAGE);
    										
    					                }
    					            }
    					        });	
    						}
    					} catch (Exception e) {
    						JFrame parent = new JFrame();
    		            	JOptionPane.showMessageDialog(parent, "Your barcode was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
    						e.printStackTrace();
    					}
                    	
                    }
                    else if(media instanceof Book){
                    	try {
    						Book book = InternetLookup.lookupBook(barcode);
    						if(book == null){
    							autoISBNLbl.setText("No matches were found");
    						}
    						else{
    							manageBookISBNTxt.setText(book.getISBN());
    							manageBookTitleTxt.setText(book.getAuthor());
    							manageBookAuthorCombo.setSelectedItem(book.getAuthor());
    							manageBookLengthTxt.setText(book.getLength());
    							manageBookYearTxt.setText(book.getYear());
    							manageBookGenreCombo.setSelectedItem(book.getGenre());
    							manageBookPlotTxt.setText(book.getPlot());
    						}
    					} catch (Exception e) {
    						JFrame parent = new JFrame();
    		            	JOptionPane.showMessageDialog(parent, "Your barcode was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
    						e.printStackTrace();
    					}
                    }
                    else if(media instanceof CD){
                    	try {
    						CD cd = InternetLookup.lookupCD(barcode);
    						if(cd == null){
    							autoISBNLbl.setText("No matches were found");
    						}
    						else{
    							manageCDISBNTxt.setText(cd.getISBN());
    							manageCDArtistCombo.setSelectedItem(cd.getAuthor());
    							manageCDAlbumTxt.setText(cd.getTitle());
    							manageCDGenreCombo.setSelectedItem(cd.getGenre());
    						}
    					} catch (Exception e) {
    						JFrame parent = new JFrame();
    		            	JOptionPane.showMessageDialog(parent, "Your barcode was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
    						e.printStackTrace();
    					}
                    }
                    else{
                    	JFrame parent = new JFrame();
    	            	JOptionPane.showMessageDialog(parent, "Error, select a media type first!", "Alert", JOptionPane.ERROR_MESSAGE);
                    }
                	}
            		
            		manageBarcodeImportLbl.setText("Your barcode " + barcode + " has been imported.");
            	}
            	catch (Exception e) {
	            	JFrame parent = new JFrame();
	            	JOptionPane.showMessageDialog(parent, "Error, there was a problem reading your barcode!", "Alert", JOptionPane.ERROR_MESSAGE);
				}
            }
        });	
		
		/**
		 * Automatically bring in data by ISBN from already entered data
		 * by taking a photo and reading the barcode so the user can edit
		 * (Requirements 1.1.2, 1.5.0, 2.0.0, 2.1.0, 2.2.0, 2.3.0, 3.0.0, 3.1.0, 3.2.0)
		 * 
		 */
		//*********************Take photo btn to search by barcode to edit or delete*************************
		manageBarcodeTakePhotoBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	manageBarcodeImportLbl.setText("");
            	
            	TakePicture.showWebcam();
           		
                (TakePicture.captureBtn).addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                    	final BufferedImage image = (TakePicture.webcam).getImage();     
                    	if(image != null){
                    		TakePicture.img = image;
                    		
                    		JLabel picLbl = new JLabel(new ImageIcon(TakePicture.img));
                    		JButton acceptBtn = new JButton("Accept");
                    		JButton retakeBtn = new JButton("Retake");
                    		JPanel buttonsPanel = new JPanel();
                    		buttonsPanel.setLayout(new FlowLayout());
                    		buttonsPanel.add(acceptBtn);
                    		buttonsPanel.add(retakeBtn);
                    		JPanel picTakenPanel = new JPanel();
                    		picTakenPanel.setLayout(new BorderLayout());
                    		picTakenPanel.add(picLbl, BorderLayout.CENTER);
                    		picTakenPanel.add(buttonsPanel, BorderLayout.SOUTH);
                    		
                            final JFrame frame2 = new JFrame();
                            frame2.setLayout(new FlowLayout());
                            frame2.add(picTakenPanel);
                            frame2.pack();
                            frame2.setTitle("Picture");
                            frame2.setVisible(true);
                    		acceptBtn.addActionListener(new java.awt.event.ActionListener() {
                                @Override
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                	if(managePanel.isVisible()){
                                		Image resizedImage = (TakePicture.img).getScaledInstance(150, -1, Image.SCALE_SMOOTH);
            							
                                		if(manageMovieEntriesPanel.isVisible()){
                                			
                                			manageBookCoverUploadStatusLbl.setIcon(null);
                                			manageCDCoverUploadStatusLbl.setIcon(null);
                                			manageMovieCoverUploadStatusLbl.setIcon(new ImageIcon(resizedImage));
            								editCoverPanel.validate();
            								ByteArrayOutputStream os = new ByteArrayOutputStream();
            								try {
												ImageIO.write(image, "png", os);
												fileInputStreamMovie = new ByteArrayInputStream(os.toByteArray());
											} catch (IOException e) {
												JFrame parent = new JFrame();
								            	JOptionPane.showMessageDialog(parent, "Your image was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
												e.printStackTrace();
											}
            							}
            							else if(manageAddBookEntriesPanel.isVisible()){
            								manageMovieCoverUploadStatusLbl.setIcon(null);
            								manageCDCoverUploadStatusLbl.setIcon(null);
            								manageBookCoverUploadStatusLbl.setIcon(new ImageIcon(resizedImage));
            								editCoverPanel.validate();
            								ByteArrayOutputStream os = new ByteArrayOutputStream();
            								try {
												ImageIO.write(image, "png", os);
												fileInputStreamBook = new ByteArrayInputStream(os.toByteArray());
											} catch (IOException e) {
												JFrame parent = new JFrame();
								            	JOptionPane.showMessageDialog(parent, "Your image was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
												e.printStackTrace();
											}
            							}
            							else if(manageAddCDEntriesPanel.isVisible()){
            								manageMovieCoverUploadStatusLbl.setIcon(null);
            								manageBookCoverUploadStatusLbl.setIcon(null);
            								manageCDCoverUploadStatusLbl.setIcon(new ImageIcon(resizedImage));
            								editCoverPanel.validate();
            								
            								ByteArrayOutputStream os = new ByteArrayOutputStream();
            								try {
												ImageIO.write(image, "png", os);
												fileInputStreamCD = new ByteArrayInputStream(os.toByteArray());
											} catch (IOException e) {
												JFrame parent = new JFrame();
								            	JOptionPane.showMessageDialog(parent, "Your image was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
												e.printStackTrace();
											}
            								
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
                                	(TakePicture.frame).dispatchEvent(new WindowEvent(TakePicture.frame, WindowEvent.WINDOW_CLOSING));
                                	(frame2).dispatchEvent(new WindowEvent(frame2, WindowEvent.WINDOW_CLOSING));
                                	(TakePicture.webcam).close();
                                }
                    		});  
                    		retakeBtn.addActionListener(new java.awt.event.ActionListener() {
                                @Override
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                	frame2.setVisible(false);
                                	TakePicture.showWebcam();
                                }
                    		});
                    	}
                    	else{
                    		JFrame parent = new JFrame();
                        	JOptionPane.showMessageDialog(parent, "Your picture was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
                    	}
                    }
                });	
            }
        });	
			
		/**
		 * Automatically bring in data by ISBN from already entered data
		 * by importing a photo and reading the barcode
		 * (Requirements 1.1.2, 1.5.0, 2.0.0, 2.1.0, 2.2.0, 2.3.0, 3.0.0, 3.1.0, 3.2.0)
		 * 
		 */
		//********************Import image to search by barcode to edit or delete**************************
		manageAutoISBNBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Connection conn = connection(databaseFilePath);
            	String barcode = manageAutoISBNTxt.getText();
            	try{
                	Media media = Search.searchByISBN(barcode, conn);
                	if(media == null){
                		JFrame parent = new JFrame();
    	            	JOptionPane.showMessageDialog(parent, "Your ISBN did not have a match in the database!", "Alert", JOptionPane.ERROR_MESSAGE);
                	}
                	else{
                	if(media instanceof Movie){
                    	try {
    						Movie movie = (Movie) media;
    						if(movie == null){
    							autoISBNLbl.setText("No matches were found");
    						}
    						else{
    							manageMovieEntriesPanel.setVisible(true);
    							manageAddBookEntriesPanel.setVisible(false);
    							manageAddCDEntriesPanel.setVisible(false);
			                	manageMovieISBNTxt.setText(movie.getISBN());
			                	manageMovieTitleTxt.setText(movie.getTitle());
			                	manageMovieDirectorCombo.setSelectedItem(movie.getAuthor());
			                	manageMovieGenreCombo.setSelectedItem(movie.getGenre());
			                	manageMovieYearTxt.setText(movie.getYear());
			                	manageMovieLengthTxt.setText(movie.getLength());
			                	manageMovieLanguageCombo.setSelectedItem(movie.getLanguage());
			                	manageMovieCountryCombo.setSelectedItem(movie.getCountry());
			                	manageMovieCastTxt.setText(movie.getCast());
			                	manageMoviePlotTxt.setText(movie.getPlot());
    							if(movie.getCover() != null){
			                	BufferedImage img = ImageIO.read(new ByteArrayInputStream(movie.getCover()));
    							Image resizedImage = img.getScaledInstance(150, -1, Image.SCALE_SMOOTH);
    							manageBookCoverUploadStatusLbl.setIcon(null);
    							manageCDCoverUploadStatusLbl.setIcon(null);
    							manageMovieCoverUploadStatusLbl.setIcon(new ImageIcon(resizedImage));
    							editCoverPanel.validate();
    							}
    						}
    					} catch (Exception e) {
    						JFrame parent = new JFrame();
    		            	JOptionPane.showMessageDialog(parent, "Your barcode was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
    						e.printStackTrace();
    					}
                    	
                    }
                    else if(media instanceof Book){
                    	try {
    						Book book = (Book)media;
    						if(book == null){
    							autoISBNLbl.setText("No matches were found");
    						}
    						else{
    							manageMovieEntriesPanel.setVisible(false);
    							manageAddBookEntriesPanel.setVisible(true);
    							manageAddCDEntriesPanel.setVisible(false);
    							manageBookISBNTxt.setText(book.getISBN());
    							manageBookTitleTxt.setText(book.getAuthor());
    							manageBookAuthorCombo.setSelectedItem(book.getAuthor());
    							manageBookLengthTxt.setText(book.getLength());
    							manageBookYearTxt.setText(book.getYear());
    							manageBookGenreCombo.setSelectedItem(book.getGenre());
    							manageBookPlotTxt.setText(book.getPlot());
    							if(book.getCover() != null){
    							BufferedImage img = ImageIO.read(new ByteArrayInputStream(book.getCover()));
    							Image resizedImage = img.getScaledInstance(150, -1, Image.SCALE_SMOOTH);
    							manageMovieCoverUploadStatusLbl.setIcon(null);
    							manageCDCoverUploadStatusLbl.setIcon(null);
    							manageBookCoverUploadStatusLbl.setIcon(new ImageIcon(resizedImage));
    							editCoverPanel.validate();
    							}
    						}
    					} catch (Exception e) {
    						JFrame parent = new JFrame();
    		            	JOptionPane.showMessageDialog(parent, "Your barcode was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
    						e.printStackTrace();
    					}
                    }
                    else if(media instanceof CD){
                    	try {
    						Media cd = (CD)media;
    						if(cd == null){
    							autoISBNLbl.setText("No matches were found");
    						}
    						else{
    							manageMovieEntriesPanel.setVisible(false);
    							manageAddBookEntriesPanel.setVisible(false);
    							manageAddCDEntriesPanel.setVisible(true);
    							manageCDISBNTxt.setText(cd.getISBN());
    							manageCDArtistCombo.setSelectedItem(cd.getAuthor());
    							manageCDAlbumTxt.setText(cd.getTitle());
    							manageCDGenreCombo.setSelectedItem(cd.getGenre());
    							if(cd.getCover() != null){
    							BufferedImage img = ImageIO.read(new ByteArrayInputStream(cd.getCover()));
    							Image resizedImage = img.getScaledInstance(150, -1, Image.SCALE_SMOOTH);
    							manageBookCoverUploadStatusLbl.setIcon(null);
    							manageMovieCoverUploadStatusLbl.setIcon(null);
								manageCDCoverUploadStatusLbl.setIcon(new ImageIcon(resizedImage));
    							editCoverPanel.validate();
    							}
    						}
    					} catch (Exception e) {
    						JFrame parent = new JFrame();
    		            	JOptionPane.showMessageDialog(parent, "Your barcode was unable to process!", "Alert", JOptionPane.ERROR_MESSAGE);
    						e.printStackTrace();
    					}
                    }
                    else{
                    	JFrame parent = new JFrame();
    	            	JOptionPane.showMessageDialog(parent, "Error, select a media type first!", "Alert", JOptionPane.ERROR_MESSAGE);
                    }
                	}
            		
            		manageBarcodeImportLbl.setText("Your barcode " + barcode + " has been imported.");
            	}
            	catch (Exception e) {
	            	JFrame parent = new JFrame();
	            	JOptionPane.showMessageDialog(parent, "Error, there was a problem reading your barcode!", "Alert", JOptionPane.ERROR_MESSAGE);
				}
            }
        });	
		
		/**
		 * Search database
		 * 
		 * (Requirements 4.4.0, 4.4.1, 4.5.0, 4.5.1)
		 * 
		 */
		/*Search "enter" btn*/
		searchEnterBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	
            	searchStatusLbl.setVisible(false);
            	Connection conn = connection(databaseFilePath);
        		String match = searchTxt.getText();
            	
            	Media[][] mediaArray = Search.searchDatabase(match, conn);
            	Movie[] movieArray = (Movie[]) mediaArray[0];
            	Book[] bookArray = (Book[]) mediaArray[1];
            	CD[] CDArray = (CD[]) mediaArray[2];
            	
            	//clear current data
            	movieModel.setRowCount(0);
            	bookModel.setRowCount(0);
            	cdModel.setRowCount(0);
            	
            	//add rows to the table, eliminate null values
            	if(movieArray != null){
            		for(int n = 0; n < movieArray.length; n++){
	            		String[] array = movieArray[n].toArray();
	            		for(int i = 0; i < array.length; i++){
	            			if(array[i] == null)
	            				array[i] = "";
	            		}
	            		try {
							BufferedImage img = ImageIO.read(new ByteArrayInputStream(movieArray[n].getCover()));
							if(img == null)
								img = logo;
							Image resizedImage = img.getScaledInstance(60, -1, Image.SCALE_SMOOTH);
							ImageIcon icon = new ImageIcon(resizedImage);
		            		String[] m = movieArray[n].toArray();
		            		movieModel.addRow(new Object[]{icon, m[0],m[1],m[2],m[3],m[4],m[5],m[6],m[7],m[8],m[9]});
		            		movieModel.fireTableRowsInserted(movieModel.getRowCount(), movieModel.getRowCount());
						} catch (IOException e) {
							JFrame parent = new JFrame();
			            	JOptionPane.showMessageDialog(parent, "There was an error in processing your search!", "Alert", JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						}
	            		
	            	}
            	}
            	if(bookArray != null){
	            	for(int n = 0; n < bookArray.length; n++){
	            		String[] array = bookArray[n].toArray();
	            		for(int i = 0; i < array.length; i++){
	            			if(array[i] == null)
	            				array[i] = "";
	            		}
						try {
							BufferedImage img = ImageIO.read(new ByteArrayInputStream(bookArray[n].getCover()));
							if(img == null)
								img = logo;
							Image resizedImage = img.getScaledInstance(60, -1, Image.SCALE_SMOOTH);
		            		ImageIcon icon = new ImageIcon(resizedImage);
		            		String[] b = bookArray[n].toArray();
		            		bookModel.addRow(new Object[] {icon, b[0],b[1],b[2],b[3],b[4],b[5],b[6]});
		            		bookModel.fireTableRowsInserted(bookModel.getRowCount(), bookModel.getRowCount());
						} catch (IOException e) {
							JFrame parent = new JFrame();
			            	JOptionPane.showMessageDialog(parent, "There was an error in processing your search!", "Alert", JOptionPane.ERROR_MESSAGE);
							
							e.printStackTrace();
						}
	            	}
            	}
            	if(CDArray != null){
	            	for(int n = 0; n < CDArray.length; n++){
	            		String[] array = CDArray[n].toArray();
	            		for(int i = 0; i < array.length; i++){
	            			if(array[i] == null)
	            				array[i] = "";
	            		}
	            		BufferedImage img;
						try {
							img = ImageIO.read(new ByteArrayInputStream(CDArray[n].getCover()));
							if(img == null)
								img = logo;
							Image resizedImage = img.getScaledInstance(60, -1, Image.SCALE_SMOOTH);
		            		ImageIcon icon = new ImageIcon(resizedImage);
		            		String[] c = CDArray[n].toArray();
		            		cdModel.addRow(new Object[] {icon,c[0],c[1],c[2],c[3]});
		            		cdModel.fireTableRowsInserted(cdModel.getRowCount(), cdModel.getRowCount());
						} catch (IOException e) {
							JFrame parent = new JFrame();
			            	JOptionPane.showMessageDialog(parent, "There was an error in processing your search!", "Alert", JOptionPane.ERROR_MESSAGE);
							
							e.printStackTrace();
						}

	            	}
            	}
            	searchStatusLbl.setText("Search complete");
            	searchStatusLbl.setVisible(true);
            }
        });		
		
		/**
		 * Display all media in the database
		 * 
		 * (Requirements 4.4.0, 4.4.1, 4.5.0, 4.5.1)
		 * 
		 */
		searchDisplayAllBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	searchStatusLbl.setVisible(false);
            	Connection conn = connection(databaseFilePath);
        		String match = "";
            	
        		Movie[] movieArray = Search.getAllMovies(conn);
        		Book[] bookArray = Search.getAllBooks(conn);
        		CD[] CDArray = Search.getAllCDs(conn);
        		
        		
            	//clear current data
            	movieModel.setRowCount(0);
            	bookModel.setRowCount(0);
            	cdModel.setRowCount(0);
            	
            	//add rows to the table, eliminate null values
            	if(movieArray != null){
            		for(int n = 0; n < movieArray.length; n++){
	            		String[] array = movieArray[n].toArray();
	            		for(int i = 0; i < array.length; i++){
	            			if(array[i] == null)
	            				array[i] = "";
	            		}
	            		try {
							BufferedImage img = ImageIO.read(new ByteArrayInputStream(movieArray[n].getCover()));
							if(img == null)
								img = logo;
							Image resizedImage = img.getScaledInstance(60, -1, Image.SCALE_SMOOTH);
							ImageIcon icon = new ImageIcon(resizedImage);
		            		String[] m = movieArray[n].toArray();
		            		movieModel.addRow(new Object[]{icon, m[0],m[1],m[2],m[3],m[4],m[5],m[6],m[7],m[8],m[9]});
		            		movieModel.fireTableRowsInserted(movieModel.getRowCount(), movieModel.getRowCount());
						} catch (IOException e) {
							JFrame parent = new JFrame();
			            	JOptionPane.showMessageDialog(parent, "There was an error in processing your search!", "Alert", JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						}
	            		
	            	}
            	}
            	if(bookArray != null){
	            	for(int n = 0; n < bookArray.length; n++){
	            		String[] array = bookArray[n].toArray();
	            		for(int i = 0; i < array.length; i++){
	            			if(array[i] == null)
	            				array[i] = "";
	            		}
						try {
							BufferedImage img = ImageIO.read(new ByteArrayInputStream(bookArray[n].getCover()));
							if(img == null)
								img = logo;
							Image resizedImage = img.getScaledInstance(60, -1, Image.SCALE_SMOOTH);
		            		ImageIcon icon = new ImageIcon(resizedImage);
		            		String[] b = bookArray[n].toArray();
		            		bookModel.addRow(new Object[] {icon, b[0],b[1],b[2],b[3],b[4],b[5],b[6]});
		            		bookModel.fireTableRowsInserted(bookModel.getRowCount(), bookModel.getRowCount());
						} catch (IOException e) {
							JFrame parent = new JFrame();
			            	JOptionPane.showMessageDialog(parent, "There was an error in processing your search!", "Alert", JOptionPane.ERROR_MESSAGE);
							
							e.printStackTrace();
						}
	            	}
            	}
            	if(CDArray != null){
	            	for(int n = 0; n < CDArray.length; n++){
	            		String[] array = CDArray[n].toArray();
	            		for(int i = 0; i < array.length; i++){
	            			if(array[i] == null)
	            				array[i] = "";
	            		}
	            		BufferedImage img;
						try {
							img = ImageIO.read(new ByteArrayInputStream(CDArray[n].getCover()));
							if(img == null)
								img = logo;
							Image resizedImage = img.getScaledInstance(60, -1, Image.SCALE_SMOOTH);
		            		ImageIcon icon = new ImageIcon(resizedImage);
		            		String[] c = CDArray[n].toArray();
		            		cdModel.addRow(new Object[] {icon,c[0],c[1],c[2],c[3]});
		            		cdModel.fireTableRowsInserted(cdModel.getRowCount(), cdModel.getRowCount());
						} catch (IOException e) {
							JFrame parent = new JFrame();
			            	JOptionPane.showMessageDialog(parent, "There was an error in processing your search!", "Alert", JOptionPane.ERROR_MESSAGE);
							
							e.printStackTrace();
						}

	            	}
            	}
            	searchStatusLbl.setText("Display all complete");
            	searchStatusLbl.setVisible(true);
            }
        });	
		
		
		/**
		 * Search database for entry by ISBN to update or delete
		 * 
		 * (Requirements 2.0.0, 2.1.0, 2.2.0, 2.3.0, 4.3.0, 4.2.0, 4.5.0, 1.5.0)
		 * 
		 */
		manageEnterBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	//only allow the panel to appear that has the result
            	movieUpdateDeleteStatusLbl.setText("");
        		bookUpdateDeleteStatusLbl.setText("");
        		CDUpdateDeleteStatusLbl.setText("");
            	
            	manageMovieISBNTxt.setText("");
            	manageMovieDirectorCombo.setSelectedIndex(0);
            	manageMovieTitleTxt.setText("");
            	manageMovieGenreCombo.setSelectedIndex(0);
            	manageMovieYearTxt.setText("");
            	manageMovieLengthTxt.setText("");
            	manageMovieLanguageCombo.setSelectedIndex(0);
            	manageMovieCountryCombo.setSelectedIndex(0);
            	manageMovieCastTxt.setText("");
            	manageMoviePlotTxt.setText("");
            	manageMovieCoverUploadStatusLbl.setIcon(null);
            	
            	manageBookISBNTxt.setText("");
            	manageBookTitleTxt.setText("");
            	manageBookAuthorCombo.setSelectedIndex(0);
            	manageBookGenreCombo.setSelectedIndex(0);
            	manageBookYearTxt.setText("");
            	manageBookPlotTxt.setText("");
            	manageBookLengthTxt.setText("");
            	manageBookCoverUploadStatusLbl.setIcon(null);
            	
            	manageCDISBNTxt.setText("");
            	manageCDArtistCombo.setSelectedIndex(0);
            	manageCDAlbumTxt.setText("");
            	manageCDGenreCombo.setSelectedIndex(0);
            	manageCDCoverUploadStatusLbl.setIcon(null);
            	
            	manageMovieEntriesPanel.setVisible(false);
        		manageAddBookEntriesPanel.setVisible(false);
        		manageAddCDEntriesPanel.setVisible(false);
            	
            	Connection conn = connection(databaseFilePath);
            	Media searchResults;
            	String ISBN = manageSearchByISBNTxt.getText();
            	if(ISBN == null)
            		ISBN = "";
            	searchResults = Search.searchByISBN(ISBN, conn);
            	if(searchResults != null){
	            	editCover = searchResults.getCover();
	                
	                currentEditID = searchResults.getId();
	                //make sure only one panel is open at a time, bring up the edit panel for the appropriate media type
	            	if(!(manageMovieEntriesPanel.isVisible() || manageAddBookEntriesPanel.isVisible() || manageAddCDEntriesPanel.isVisible())){
		                if(searchResults instanceof Movie){
		                	manageMovieEntriesPanel.setVisible(true);
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
		                	manageAddBookEntriesPanel.setVisible(true);
		                	manageBookISBNTxt.setText(searchResults.getISBN());
		                	manageBookTitleTxt.setText(searchResults.getTitle());
		                	manageBookAuthorCombo.setSelectedItem(searchResults.getAuthor());
		                	manageBookYearTxt.setText(((Book) searchResults).getYear());
		                	manageBookGenreCombo.setSelectedItem(searchResults.getGenre());
		                	manageBookLengthTxt.setText(((Book) searchResults).getLength());
		                	manageBookPlotTxt.setText(((Book) searchResults).getPlot());
		                	}
		                else if(searchResults instanceof CD){
		                	manageAddCDEntriesPanel.setVisible(true);
		                	manageCDISBNTxt.setText(searchResults.getISBN());
		                	manageCDArtistCombo.setSelectedItem(searchResults.getAuthor());
		                	manageCDAlbumTxt.setText(searchResults.getTitle());
		                	manageCDGenreCombo.setSelectedItem(searchResults.getGenre());
		                }
		                
		            }
	            	else{
	            		JFrame parent = new JFrame();
		            	JOptionPane.showMessageDialog(parent, "You already have an edit/delete page up! Please close the current before opening another.", "Alert", JOptionPane.ERROR_MESSAGE);
	             	}
            	}
            	else{
            		JFrame parent = new JFrame();
	            	JOptionPane.showMessageDialog(parent, "No match was found.", "Alert", JOptionPane.INFORMATION_MESSAGE);
            	}
            }
        });		

	
		/**
		 * Search database for entry by title to update or delete
		 * 
		 * (Requirements 2.0.0, 2.1.0, 2.2.0, 2.3.0, 4.3.0, 4.2.0, 4.5.0, 1.5.0)
		 * 
		 */
		/*Search for media by title*/
		manageEnterTitleBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	//only allow the panel to appear that has the result
            	manageMovieEntriesPanel.setVisible(false);
        		manageAddBookEntriesPanel.setVisible(false);
        		manageAddCDEntriesPanel.setVisible(false);
        		
        		movieUpdateDeleteStatusLbl.setText("");
        		bookUpdateDeleteStatusLbl.setText("");
        		CDUpdateDeleteStatusLbl.setText("");
        		
        		//start with blank fields
        		manageMovieISBNTxt.setText("");
            	manageMovieDirectorCombo.setSelectedIndex(0);
            	manageMovieTitleTxt.setText("");
            	manageMovieGenreCombo.setSelectedIndex(0);
            	manageMovieYearTxt.setText("");
            	manageMovieLengthTxt.setText("");
            	manageMovieLanguageCombo.setSelectedIndex(0);
            	manageMovieCountryCombo.setSelectedIndex(0);
            	manageMovieCastTxt.setText("");
            	manageMoviePlotTxt.setText("");
            	manageMovieCoverUploadStatusLbl.setIcon(null);
            	
            	manageBookISBNTxt.setText("");
            	manageBookTitleTxt.setText("");
            	manageBookAuthorCombo.setSelectedIndex(0);
            	manageBookGenreCombo.setSelectedIndex(0);
            	manageBookYearTxt.setText("");
            	manageBookPlotTxt.setText("");
            	manageBookLengthTxt.setText("");
            	manageBookCoverUploadStatusLbl.setIcon(null);
            	
            	manageCDISBNTxt.setText("");
            	manageCDArtistCombo.setSelectedIndex(0);
            	manageCDAlbumTxt.setText("");
            	manageCDGenreCombo.setSelectedIndex(0);
            	manageCDCoverUploadStatusLbl.setIcon(null);
        		
            	Connection conn = connection(databaseFilePath);
            	Media searchResults;
            	String title = manageSearchByTitleTxt.getText();
            	if(title == null)
            		title = "";
            	searchResults = Search.searchByTitle(title, conn);
            	if(searchResults != null){
	            	editCover = searchResults.getCover();
	                
	                currentEditID = searchResults.getId();
	                //make sure only one panel is open at a time, bring up the edit panel for the appropriate media type
	            	if(!(manageMovieEntriesPanel.isVisible() || manageAddBookEntriesPanel.isVisible() || manageAddCDEntriesPanel.isVisible())){
		                if(searchResults instanceof Movie){
		                	manageMovieEntriesPanel.setVisible(true);
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
		                	manageAddBookEntriesPanel.setVisible(true);
		                	manageBookISBNTxt.setText(searchResults.getISBN());
		                	manageBookTitleTxt.setText(searchResults.getTitle());
		                	manageBookAuthorCombo.setSelectedItem(searchResults.getAuthor());
		                	manageBookYearTxt.setText(((Book) searchResults).getYear());
		                	manageBookGenreCombo.setSelectedItem(searchResults.getGenre());
		                	manageBookLengthTxt.setText(((Book) searchResults).getLength());
		                	manageBookPlotTxt.setText(((Book) searchResults).getPlot());
		                	}
		                else if(searchResults instanceof CD){
		                	manageAddCDEntriesPanel.setVisible(true);
		                	manageCDISBNTxt.setText(searchResults.getISBN());
		                	manageCDArtistCombo.setSelectedItem(searchResults.getAuthor());
		                	manageCDAlbumTxt.setText(searchResults.getTitle());
		                	manageCDGenreCombo.setSelectedItem(searchResults.getGenre());
		                }
		                
		            }
	            	else{
	            		JFrame parent = new JFrame();
		            	JOptionPane.showMessageDialog(parent, "You already have an edit/delete page up! Please close the current before opening another.", "Alert", JOptionPane.ERROR_MESSAGE);
	             	}
            	}
            	else{
            		JFrame parent = new JFrame();
	            	JOptionPane.showMessageDialog(parent, "No match was found.", "Alert", JOptionPane.INFORMATION_MESSAGE);
            	}
                
            }
        });	
		
		/**
		 * Search database for entry by ISBN (barcode reader) to update or delete
		 * 
		 * (Requirements 2.0.0, 2.1.0, 2.2.0, 2.3.0, 3.1.0, 3.2.0, 4.3.0, 
		 * 4.2.0, 4.5.0)
		 * 
		 */
		/*trigger popup when search by barcode is clicked*/
		manageSearchByBarcodeBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	manageBarcodeImportLbl.setText("");
            	//only allow the panel to appear that has the result
            	manageMovieEntriesPanel.setVisible(false);
        		manageAddBookEntriesPanel.setVisible(false);
        		manageAddCDEntriesPanel.setVisible(false);
        		
        		movieUpdateDeleteStatusLbl.setText("");
        		bookUpdateDeleteStatusLbl.setText("");
        		CDUpdateDeleteStatusLbl.setText("");
        		
        		manageMovieISBNTxt.setText("");
            	manageMovieDirectorCombo.setSelectedIndex(0);
            	manageMovieTitleTxt.setText("");
            	manageMovieGenreCombo.setSelectedIndex(0);
            	manageMovieYearTxt.setText("");
            	manageMovieLengthTxt.setText("");
            	manageMovieLanguageCombo.setSelectedIndex(0);
            	manageMovieCountryCombo.setSelectedIndex(0);
            	manageMovieCastTxt.setText("");
            	manageMoviePlotTxt.setText("");
            	manageMovieCoverUploadStatusLbl.setIcon(null);
            	
            	manageBookISBNTxt.setText("");
            	manageBookTitleTxt.setText("");
            	manageBookAuthorCombo.setSelectedIndex(0);
            	manageBookGenreCombo.setSelectedIndex(0);
            	manageBookYearTxt.setText("");
            	manageBookPlotTxt.setText("");
            	manageBookLengthTxt.setText("");
            	manageBookCoverUploadStatusLbl.setIcon(null);
            	
            	manageCDISBNTxt.setText("");
            	manageCDArtistCombo.setSelectedIndex(0);
            	manageCDAlbumTxt.setText("");
            	manageCDGenreCombo.setSelectedIndex(0);
            	manageCDCoverUploadStatusLbl.setIcon(null);
        		
            	JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(manageBarcodeImagePopupPanel);
                frame.pack();
                frame.setTitle("Image import");
                frame.setVisible(true);
            }
        });		
		
		/**
		 * Edit Movie
		 * 
		 * (Requirements 2.0.0, 2.3.0, 4.3.0, 1.5.0, 1.3.4)
		 * 
		 */
		/*Updates movie entry*/
		manageEditMovieBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JPanel confirmPanel = new JPanel();
            	confirmPanel.setLayout(new FlowLayout());
            	JLabel confirmLbl = new JLabel("Are you sure you would like to update this movie?");
            	JButton yesBtn = new JButton("Yes");
            	JButton noBtn = new JButton("No");
            	confirmPanel.add(confirmLbl);
            	confirmPanel.add(yesBtn);
            	confirmPanel.add(noBtn);
            	final JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(confirmPanel);
                frame.pack();
                frame.setTitle("Confirm Update");
                frame.setVisible(true);
		
                //if user confirms to update, then update
				yesBtn.addActionListener(new java.awt.event.ActionListener() {
		            @Override
		            public void actionPerformed(java.awt.event.ActionEvent evt) {
		            	frame.setVisible(false);
		            	Connection conn = connection(databaseFilePath);
		            	//extract data from entered fields to create Movie to send to be updated
		            	String ISBN = manageMovieISBNTxt.getText();
		            	if(ISBN == null)
		            		ISBN = "";
		            	String title = manageMovieTitleTxt.getText();
		            	if(title == null)
		            		title = "";
		            	String director = (String) manageMovieDirectorCombo.getSelectedItem();
		            	if(director == null)
		            		director = "";
		            	String genre = (String) manageMovieGenreCombo.getSelectedItem();
		            	if(genre == null)
		            		genre = "";
		            	String year = manageMovieYearTxt.getText();
		            	if(year == null)
		            		year = "";
		            	String length = manageMovieLengthTxt.getText();
		            	if(length == null)
		            		length = "";
		            	String language = (String) manageMovieLanguageCombo.getSelectedItem();
		            	if(language == null)
		            		language = "";
		            	String country = (String) manageMovieCountryCombo.getSelectedItem();
		            	if(country == null)
		            		country = "";
		            	String cast = manageMovieCastTxt.getText();
		            	if(cast == null)
		            		cast = "";
		            	String plot = manageMoviePlotTxt.getText();
		            	if(plot == null)
		            		plot = "";
		            	Image image = (Image) manageMovieCoverUploadStatusLbl.getIcon();
		            	
		            	if(!(title.equals("") || title == null)){
			            	Movie movie = new Movie(currentEditID, title, director, ISBN, genre, editCover, year, plot, cast, length, language, country);
			            	try {
								Database.update(movie);
								movieUpdateDeleteStatusLbl.setText("Your movie " + title + " has been updated!");
							} catch (Exception e) {
								e.printStackTrace();
								JFrame parent = new JFrame();
				            	JOptionPane.showMessageDialog(parent, "Error! Your edit did not process.", "Alert", JOptionPane.ERROR_MESSAGE);
				            
							}
		            	}
		            	else{
			        		JFrame parent = new JFrame();
			            	JOptionPane.showMessageDialog(parent, "Error! You must have an entry in the title.", "Alert", JOptionPane.ERROR_MESSAGE);
			            
		            	}
		            	
		            }
		        });	
				//else close frame
				noBtn.addActionListener(new java.awt.event.ActionListener() {
		            @Override
		            public void actionPerformed(java.awt.event.ActionEvent evt) {
		            	frame.setVisible(false);
		            }
		        });	
		    }
        });	
		
		/**
		 * Delete a movie entry
		 * 
		 * (Requirements 2.0.0, 2.3.0, 4.2.0, 1.5.0, 1.3.2)
		 * 
		 */
		/*Deletes movie entry*/
		manageMovieDeleteBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JPanel confirmPanel = new JPanel();
            	confirmPanel.setLayout(new FlowLayout());
            	JLabel confirmLbl = new JLabel("Are you sure you would like to delete this movie?");
            	JButton yesBtn = new JButton("Yes");
            	JButton noBtn = new JButton("No");
            	confirmPanel.add(confirmLbl);
            	confirmPanel.add(yesBtn);
            	confirmPanel.add(noBtn);
            	final JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(confirmPanel);
                frame.pack();
                frame.setTitle("Confirm Delete");
                frame.setVisible(true);
				yesBtn.addActionListener(new java.awt.event.ActionListener() {
		            @Override
		            public void actionPerformed(java.awt.event.ActionEvent evt) {
		            	frame.setVisible(false);
		            	Connection conn = connection(databaseFilePath);
		            	
		            	String ISBN = manageMovieISBNTxt.getText();
		            	if(ISBN == null)
		            		ISBN = "";
		            	String title = manageMovieTitleTxt.getText();
		            	if(title == null)
		            		title = "";
		            	String director = (String) manageMovieDirectorCombo.getSelectedItem();
		            	if(director == null)
		            		director = "";
		            	String genre = (String) manageMovieGenreCombo.getSelectedItem();
		            	if(genre == null)
		            		genre = "";
		            	String year = manageMovieYearTxt.getText();
		            	if(year == null)
		            		year = "";
		            	String length = manageMovieLengthTxt.getText();
		            	if(length == null)
		            		length = "";
		            	String language = (String) manageMovieLanguageCombo.getSelectedItem();
		            	if(language == null)
		            		language = "";
		            	String country = (String) manageMovieCountryCombo.getSelectedItem();
		            	if(country == null)
		            		country = "";
		            	String cast = manageMovieCastTxt.getText();
		            	if(cast == null)
		            		cast = "";
		            	String plot = manageMoviePlotTxt.getText();
		            	if(plot == null)
		            		plot = "";
		            	
		            	if(!(title.equals("") || title == null)){	
			            	Movie movie = new Movie(currentEditID, title, director, ISBN, genre, editCover, year, plot, cast, length, language, country);
			            	try {
								Database.delete(movie);
								movieUpdateDeleteStatusLbl.setText("Your movie " + title + " has been deleted!");
							} catch (Exception e) {
								e.printStackTrace();
								JFrame parent = new JFrame();
				            	JOptionPane.showMessageDialog(parent, "Error! Your edit did not process.", "Alert", JOptionPane.ERROR_MESSAGE);
				            
							}
		            	}
		            	else{
			        		JFrame parent = new JFrame();
			            	JOptionPane.showMessageDialog(parent, "Error! You must have an entry in the title.", "Alert", JOptionPane.ERROR_MESSAGE);
			            
		            	}
			            }
			        });	
				//else close frame
				noBtn.addActionListener(new java.awt.event.ActionListener() {
		            @Override
		            public void actionPerformed(java.awt.event.ActionEvent evt) {
		            	frame.setVisible(false);
		            }
		        });	
            }
        });	
		
		/**
		 * Edit Book
		 * 
		 * (Requirements 2.0.0, 2.1.0, 4.3.0, 1.5.0, 1.3.4)
		 * 
		 */
		/*Updates book entry*/
		
		manageBookEditBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JPanel confirmPanel = new JPanel();
            	confirmPanel.setLayout(new FlowLayout());
            	JLabel confirmLbl = new JLabel("Are you sure you would like to update this book?");
            	JButton yesBtn = new JButton("Yes");
            	JButton noBtn = new JButton("No");
            	confirmPanel.add(confirmLbl);
            	confirmPanel.add(yesBtn);
            	confirmPanel.add(noBtn);
            	final JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(confirmPanel);
                frame.pack();
                frame.setTitle("Confirm Update");
                frame.setVisible(true);
				yesBtn.addActionListener(new java.awt.event.ActionListener() {
	            @Override
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	frame.setVisible(false);
	            	Connection conn = connection(databaseFilePath);
	            	
	            	String ISBN = manageBookISBNTxt.getText();
	            	if(ISBN == null)
	            		ISBN = "";
	            	String title = manageBookTitleTxt.getText();
	            	if(title == null)
	            		title = "";
	            	String author = (String) manageBookAuthorCombo.getSelectedItem();
	            	if(author == null)
	            		author = "";
	            	String yearPublished = manageBookYearTxt.getText();
	            	if(yearPublished == null)
	            		yearPublished = "";
	            	String plot = manageBookPlotTxt.getText();
	            	if(plot == null)
	            		plot = "";
	            	String genre = (String) manageBookGenreCombo.getSelectedItem();
	            	if(genre == null)
	            		genre = "";
	            	String year = manageBookYearTxt.getText();
	            	if(year == null)
	            		year = "";
	            	String length = manageBookLengthTxt.getText();
	            	if(length == null)
	            		length = "";
	            	
	            	if(!(title.equals("") || title == null)){
		            	Book book = new Book(currentEditID, title, author, ISBN, genre, editCover, year, plot, length);
		            	try {
							Database.update(book);
							bookUpdateDeleteStatusLbl.setText("Your book " + title + " has been updated!");
						} catch (Exception e) {
							e.printStackTrace();
							JFrame parent = new JFrame();
			            	JOptionPane.showMessageDialog(parent, "Error! Your delete did not process.", "Alert", JOptionPane.ERROR_MESSAGE);
			            
						}
	            	}
	            	else{
		        		JFrame parent = new JFrame();
		            	JOptionPane.showMessageDialog(parent, "Error! You must have an entry in the title.", "Alert", JOptionPane.ERROR_MESSAGE);
		            
	            	}
			            }
			        });	
				//else close frame
				noBtn.addActionListener(new java.awt.event.ActionListener() {
			        @Override
			        public void actionPerformed(java.awt.event.ActionEvent evt) {
			        	frame.setVisible(false);
			        }
			    });	
            	
            }
        });	

		/**
		 * Delete a Book
		 * 
		 * (Requirements 2.0.0, 2.1.0, 4.2.0, 1.5.0, 1.3.2)
		 * 
		 */
		/*Deletes book entry*/
		manageBookDeleteBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JPanel confirmPanel = new JPanel();
            	confirmPanel.setLayout(new FlowLayout());
            	JLabel confirmLbl = new JLabel("Are you sure you would like to delete this book?");
            	JButton yesBtn = new JButton("Yes");
            	JButton noBtn = new JButton("No");
            	confirmPanel.add(confirmLbl);
            	confirmPanel.add(yesBtn);
            	confirmPanel.add(noBtn);
            	final JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(confirmPanel);
                frame.pack();
                frame.setTitle("Confirm Delete");
                frame.setVisible(true);
				yesBtn.addActionListener(new java.awt.event.ActionListener() {
	            @Override
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	frame.setVisible(false);
	            	Connection conn = connection(databaseFilePath);
	            	
	            	String ISBN = manageBookISBNTxt.getText();
	            	String title = manageBookTitleTxt.getText();
	            	String author = (String) manageBookAuthorCombo.getSelectedItem();
	            	String yearPublished = manageBookYearTxt.getText();
	            	String plot = manageBookPlotTxt.getText();
	            	String genre = (String) manageBookGenreCombo.getSelectedItem();
	            	String year = manageBookYearTxt.getText();
	            	String length = manageBookLengthTxt.getText();
	            	
	            	if(!(title.equals("") || title == null)){
		            	Book book = new Book(currentEditID, title, author, ISBN, genre, editCover, year, plot, length);
		            	try {
							Database.delete(book);
						bookUpdateDeleteStatusLbl.setText("Your book " + title + " has been deleted!");
						} catch (Exception e) {
							e.printStackTrace();
							JFrame parent = new JFrame();
			            	JOptionPane.showMessageDialog(parent, "Error! Your delete did not process.", "Alert", JOptionPane.ERROR_MESSAGE);
			            
						}
	            	}
	            	else{
		        		JFrame parent = new JFrame();
		            	JOptionPane.showMessageDialog(parent, "Error! You must have an entry in the title.", "Alert", JOptionPane.ERROR_MESSAGE);
		            
	            	}
			            }
			        });	
				//else close frame
				noBtn.addActionListener(new java.awt.event.ActionListener() {
			        @Override
			        public void actionPerformed(java.awt.event.ActionEvent evt) {
			        	frame.setVisible(false);
			        }
			    });	
            }
            	
        });	

		/**
		 * Edit CD
		 * 
		 * (Requirements 2.0.0, 2.2.0, 4.3.0, 1.5.0, 1.3.4)
		 * 
		 */
		/*Updates CD entry*/
		manageCDEditBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JPanel confirmPanel = new JPanel();
            	confirmPanel.setLayout(new FlowLayout());
            	JLabel confirmLbl = new JLabel("Are you sure you would like to update this CD?");
            	JButton yesBtn = new JButton("Yes");
            	JButton noBtn = new JButton("No");
            	confirmPanel.add(confirmLbl);
            	confirmPanel.add(yesBtn);
            	confirmPanel.add(noBtn);
            	final JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(confirmPanel);
                frame.pack();
                frame.setTitle("Confirm Update");
                frame.setVisible(true);
				yesBtn.addActionListener(new java.awt.event.ActionListener() {
	            @Override
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	frame.setVisible(false);
	            	Connection conn = connection(databaseFilePath);
	            	
	            	String ISBN = manageCDISBNTxt.getText();
	            	String artist = (String) manageCDArtistCombo.getSelectedItem();
	            	String album = manageCDAlbumTxt.getText();
	            	String genre = (String)manageCDGenreCombo.getSelectedItem();
	            	
	            	if(!(album.equals("") || album == null)){
		            	CD cd = new CD(currentEditID, album, artist, ISBN, genre, editCover);
		            	try {
		            		CDUpdateDeleteStatusLbl.setText("Your CD " + album + " has been updated!");
							Database.update(cd);
						} catch (Exception e) {
							e.printStackTrace();
							JFrame parent = new JFrame();
			            	JOptionPane.showMessageDialog(parent, "Error! Your delete did not process.", "Alert", JOptionPane.ERROR_MESSAGE);
			            
						}
	            	}
	            	else{
		        		JFrame parent = new JFrame();
		            	JOptionPane.showMessageDialog(parent, "Error! You must have an entry for the album.", "Alert", JOptionPane.ERROR_MESSAGE);
		            
	            	}
			            }
			        });	
				//else close frame
				noBtn.addActionListener(new java.awt.event.ActionListener() {
			        @Override
			        public void actionPerformed(java.awt.event.ActionEvent evt) {
			        	frame.setVisible(false);
			        }
			    });	
            }
        });	

		/**
		 * Delete CD
		 * 
		 * (Requirements 2.0.0, 2.2.0, 4.2.0, 1.5.0, 1.3.2)
		 * 
		 */
		/*Deletes CD entry*/
		manageCDDeleteBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JPanel confirmPanel = new JPanel();
            	confirmPanel.setLayout(new FlowLayout());
            	JLabel confirmLbl = new JLabel("Are you sure you would like to delete this CD?");
            	JButton yesBtn = new JButton("Yes");
            	JButton noBtn = new JButton("No");
            	confirmPanel.add(confirmLbl);
            	confirmPanel.add(yesBtn);
            	confirmPanel.add(noBtn);
            	final JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.add(confirmPanel);
                frame.pack();
                frame.setTitle("Confirm Delete");
                frame.setVisible(true);
				yesBtn.addActionListener(new java.awt.event.ActionListener() {
	            @Override
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	frame.setVisible(false);
	            	Connection conn = connection(databaseFilePath);
	            	
	            	String ISBN = manageCDISBNTxt.getText();
	            	String artist = (String) manageCDArtistCombo.getSelectedItem();
	            	String album = manageCDAlbumTxt.getText();
	            	String genre = (String)manageCDGenreCombo.getSelectedItem();
	            	
	            	if(!(album.equals("") || album == null)){
		            	CD cd = new CD(currentEditID, album, artist, ISBN, genre, editCover);
		            	try {
							Database.delete(cd);
							CDUpdateDeleteStatusLbl.setText("Your CD " + album + " has been deleted!");
						} catch (Exception e) {
							e.printStackTrace();
							JFrame parent = new JFrame();
			            	JOptionPane.showMessageDialog(parent, "Error! Your delete did not process.", "Alert", JOptionPane.ERROR_MESSAGE);
			            
						}
	            	}
	            	else{
		        		JFrame parent = new JFrame();
		            	JOptionPane.showMessageDialog(parent, "Error! You must have an entry for the album.", "Alert", JOptionPane.ERROR_MESSAGE);
		            
	            	}
			            }
			        });	
				//else close frame
				noBtn.addActionListener(new java.awt.event.ActionListener() {
			        @Override
			        public void actionPerformed(java.awt.event.ActionEvent evt) {
			        	frame.setVisible(false);
			        }
			    });	
	            }
        });	
	    
	}

	public static void main(String[] args) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
			System.out.println("failed");
		}
		MediaLibrary frame = new MediaLibrary();
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(new Dimension(1100,700));
		frame.setTitle("Media Library");
		frame.setVisible(true);
		
	}

}
