package media;
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
 * @version 1.1.1 2/19/15
 */

public class mediaLibrary extends JFrame{
	
	public mediaLibrary(){
		//********************connect with the database with SQLite***********************
		LinkedList countryList = new LinkedList();
		LinkedList languageList = new LinkedList();
		LinkedList genreList = new LinkedList();
		try {
					Class.forName("org.sqlite.JDBC");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				Connection conn;
				Statement stat;
				try {
					conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/jeremy/workspace/Media Library/database.db");
					stat = conn.createStatement();
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
		addMovieEntriesPanel.setLayout(new GridLayout(13, 3, 10, 10));
		JLabel movieCoverUploadStatusLbl = new JLabel();
		
		/*Image image = new Image(getClass().getResourceAsStream(filePath));*/
		JLabel placeHolder2 = new JLabel();
		JLabel placeholder = new JLabel();
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
		addMovieEntriesPanel.add(movieCoverUploadStatusLbl);
		addMovieEntriesPanel.add(placeHolder2);
		addMovieEntriesPanel.add(placeholder);
		addMovieEntriesPanel.add(movieIDLbl);
		addMovieEntriesPanel.add(movieID);
		addMovieEntriesPanel.add(movieIDBlank);
		addMovieEntriesPanel.add(movieTitleLbl);
		addMovieEntriesPanel.add(movieTitleTxt);
		addMovieEntriesPanel.add(movieTitleBlank);
		addMovieEntriesPanel.add(movieDirectorLbl);
		addMovieEntriesPanel.add(movieDirectorTxt);
		addMovieEntriesPanel.add(movieDirectorBlank);
		addMovieEntriesPanel.add(movieGenreLbl);
		addMovieEntriesPanel.add(movieGenreCombo);
		addMovieEntriesPanel.add(movieGenreNewBtn);
		addMovieEntriesPanel.add(movieYearLbl);
		addMovieEntriesPanel.add(movieYearTxt);
		addMovieEntriesPanel.add(movieYearBlank);
		addMovieEntriesPanel.add(movieLengthLbl);
		addMovieEntriesPanel.add(movieLengthTxt);
		addMovieEntriesPanel.add(movieLengthBlank);
		addMovieEntriesPanel.add(movieLanguageLbl);
		addMovieEntriesPanel.add(movieLanguageCombo);
		addMovieEntriesPanel.add(movieLanguageNewBtn);
		addMovieEntriesPanel.add(movieCountryLbl);
		addMovieEntriesPanel.add(movieCountryCombo);
		addMovieEntriesPanel.add(movieCountryNewBtn);
		addMovieEntriesPanel.add(movieCastLbl);
		addMovieEntriesPanel.add(movieCastTxt);
		addMovieEntriesPanel.add(movieCastBlank);
		addMovieEntriesPanel.add(moviePlotLbl);
		addMovieEntriesPanel.add(moviePlotTxt);
		addMovieEntriesPanel.add(moviePlotBlank);
		addMovieEntriesPanel.add(placeHolder1);
		addMovieEntriesPanel.add(addMovieBtn);
		
		/*text fields for adding book content*/
		final JPanel addBookEntriesPanel = new JPanel();
		addBookEntriesPanel.setLayout(new GridLayout(6,2,10,10));
		JLabel bookCoverUploadStatusLbl = new JLabel();
		JLabel placeHolder3 = new JLabel();
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
		addBookEntriesPanel.add(bookCoverUploadStatusLbl);
		addBookEntriesPanel.add(placeHolder3);
		addBookEntriesPanel.add(bookTitleLbl);
		addBookEntriesPanel.add(bookTitleTxt);
		addBookEntriesPanel.add(bookAuthorLbl);
		addBookEntriesPanel.add(bookAuthorTxt);
		addBookEntriesPanel.add(bookYearLbl);
		addBookEntriesPanel.add(bookYearTxt);
		addBookEntriesPanel.add(bookPlotLbl);
		addBookEntriesPanel.add(bookPlotTxt);
		addBookEntriesPanel.add(placeHolder4);
		addBookEntriesPanel.add(addBookBtn);
		
		/*text fields for adding CD content*/
		final JPanel addCDEntriesPanel = new JPanel();
		addCDEntriesPanel.setLayout(new GridLayout(6,3,10,10));
		JLabel CDCoverUploadStatusLbl = new JLabel();
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
		addCDEntriesPanel.add(CDCoverUploadStatusLbl);
		addCDEntriesPanel.add(placeHolder5);
		addCDEntriesPanel.add(placeholder2);
		addCDEntriesPanel.add(CDArtistLbl);
		addCDEntriesPanel.add(CDArtistTxt);
		addCDEntriesPanel.add(CDArtistBlank);
		addCDEntriesPanel.add(CDAlbumLbl);
		addCDEntriesPanel.add(CDAlbumTxt);
		addCDEntriesPanel.add(CDAlbumBlank);
		addCDEntriesPanel.add(CDGenreLbl);
		addCDEntriesPanel.add(CDGenreCombo);
		addCDEntriesPanel.add(CDGenreNewBtn);
		addCDEntriesPanel.add(placeHolder6);
		addCDEntriesPanel.add(addCDBtn);
		
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
		JButton importImageBtn = new JButton("Import Image");
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
		JPanel lookupPanel = new JPanel();
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
		JPanel managePanel = new JPanel();
		managePanel.setLayout(new FlowLayout());
		JButton manageSearchByBarcodeBtn = new JButton("Search by Barcode");
		JButton manageSearchByCoverPhotoBtn = new JButton("Search by Cover Photo");
		managePanel.add(manageTopPanel);
		managePanel.add(manageSearchByBarcodeBtn);
		managePanel.add(manageSearchByCoverPhotoBtn);
		
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
		
		//*********************TODO:Manage "enter" btn*************************
		manageEnterBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
            }
        });		
		
		//*********************TODO:Manage "Add CD genre" btn*************************
		CDGenreNewBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
            }
        });	
				
		//*********************TODO:Manage "Add movie genre" btn*************************
		movieGenreNewBtn.addActionListener(new java.awt.event.ActionListener() {
	        @Override
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	            
	        }    
	    });	
		
		//*********************TODO:Manage "Add movie language btn" btn*************************
		movieLanguageNewBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
            }
        });	
		
		//*********************TODO:Manage "Add movie country" btn*************************
		movieCountryNewBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
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
