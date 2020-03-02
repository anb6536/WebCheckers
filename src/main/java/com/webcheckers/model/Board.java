package com.webcheckers.model;

import java.util.ArrayList;
import java.util.List;

import static com.webcheckers.model.Piece.color.RED;
import static com.webcheckers.model.Piece.color.WHITE;

public class Board {
    public Board(){

    }

    /**
     * create a BoardView with correct starting positions
     * @return the start of a board
     */
    public BoardView makeBoard(){

        // create the list of rows
        List<Row> rows = new ArrayList<>();

        // add a row to the list 8 times
        for ( int i=0 ; i<8 ; i++ ){

            // create the list of spaces for this row
            List<Space> spaces = new ArrayList<>();

            // add a space to the row 8 times
            for ( int j=0 ; j<8 ; j++ ){
                Space space = new Space(j);

                // if it is an even row
                if ( i%2 == 0 ){

                    // if it is an even row and a even column
                    if ( j%2 == 0 ){
                        // set the color to light
                        space.setColor(Space.Color.LIGHT);
                    }
                    // if it is an even row and an odd column
                    else {
                        // set the color to dark
                        space.setColor(Space.Color.DARK);
                    }
                }
                // if it is an odd row
                else {
                    // if it is an odd row and an odd column
                    if ( j%2 != 0 ){
                        // set the color to light
                        space.setColor(Space.Color.LIGHT);
                    }
                    // if it is an odd row and an even column
                    else {
                        // set the color to dark
                        space.setColor(Space.Color.DARK);
                    }
                }
                // add the list of spaces to the row
                spaces.add(space);
            }
            // add the row to the list of rows
            Row row = new Row(i, spaces);
            rows.add(row);
        }
        // return the boardview of the board
        BoardView boardView = new BoardView(rows);
        return boardView;
    }

    /**
     *
     * @param boardView the reference of the board filled with spaces
     */
    public void addPiece( BoardView boardView ){
        // get the rows
        List<Row> rows = boardView.getRows();
        int i = 0;

        // for every row, add pieces to every proper space
        for ( Row row : rows ){
            // get the spaces for this row
            List<Space> spaces = row.getSpaces();
            for ( Space space : spaces ){
                // if the row should be filled with red pieces
                if ( i>=5 ){
                    // if the space is a dark square
                    if ( space.isValid() ){
                        // add a red piece to the space on the board
                        Piece piece = new Piece(RED);
                        space.setPiece(piece);
                    }
                }
                // if the row should be filled with white pieces
                else if (i<=2){
                    // if the space is a dark square
                    if ( space.isValid() ){
                        // add a white piece to the space on the board
                        Piece piece = new Piece(WHITE);
                        space.setPiece(piece);
                    }
                }
            }
            i++;
        }
    }

    /**
     * flip the board so that the other player has a correct facing board
     * @param boardView the filled out board for a game that is facing the original way
     * @return the flipped board
     */
    public BoardView flipBoard( BoardView boardView ){
        Row[] rows = new Row[8];
        List<Row> rows1 = boardView.getRows();
        List<Row> newRow = new ArrayList<>();

        // start at the other end of the board
        int i = 7;
        // for every row in the original board
        for ( Row row : rows1 ){
            Space[] spaces = new Space[8];
            List<Space> spaces1 = row.getSpaces();
            List<Space> newSpaces = new ArrayList<>();
            int j=7;

            // for every space in the original row
            for ( Space space : spaces1 ){
                spaces[j] = space;
                j--;
            }
            for ( int k=0 ; k<8 ; k++ ){
                newSpaces.add(spaces[k]);
            }
            // make a new row  with the correct index
            Row newRow1 = new Row(row.getIndex(), newSpaces);
            rows[i] = newRow1;
            i--;
        }
        for (int j=0 ; j<8 ; j++){
            newRow.add(rows[j]);
        }

        // return the flipped board
        BoardView boardView1 = new BoardView(newRow);
        return boardView1;
    }
}
