package src;

public class DataMatrix implements BarcodeIO
{
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';  

   private BarcodeImage image;
   private String text;
   private int actualWidth; // dependent on the data in the image. Can change as image changes
   private int actualHeight; // and can be computed from the "spine" of the image.

   
   /**
    *  Default constructor that takes no arguments and creates an blank image and empty text
    */ 
   public DataMatrix()
   {
      // Create empty default member variables 
      image = new BarcodeImage();
      text = "";
      actualWidth = 0;
      actualHeight = 0;
   }

   /**
    * Constructor that takes a BarcodeImage object as an argument
    * @param image
    */
   public DataMatrix(final BarcodeImage image) 
   {
      if(image == null){
         return;
      }
      scan(image); // read in the image
      text = "";
   }

   /**
    * Constructor that takes in a text String
    * @param text
    */
   public DataMatrix(final String text) 
   {
      image = new BarcodeImage();
      actualWidth = 0;
      actualHeight = 0;
      readText(text); // read in the text
   }

   /**
    * Method to read in a text String
    * @param text 
    */
   public boolean readText(final String text) 
   {
      if(text == null)
         return false;

      this.text = text;
      return true;
   }

   /**
    * Method to read in the image, make a copy of it and clean it up
    * @param image
    * @return 
    */
   public boolean scan(final BarcodeImage image) 
   {
      try {
         this.image = image.clone();
         cleanImage();
         actualWidth = computeSignalWidth(); 
         actualHeight = computeSignalHeight(); 
         return true;
      } catch (CloneNotSupportedException e) {
         return false;
      }
   }

   /**
    * Accessor for actualWidth
    */ 
   public int getActualWidth()
   {
      return actualWidth;
   }

   /**
    * Accessor for actualHeight
    */ 
   public int getActualHeight()
   {
      return actualHeight;
   }

   /**
    * creates a barcode image from a String value 
    * @return boolean
    */
   public boolean generateImageFromText() 
   {
      image = new BarcodeImage();

      int textLength = text.length();

      //adds 2 to the width for border 
      actualWidth = textLength + 2;

      //8 rows in the image plus the border width
      actualHeight = 10;

      //intial image creation 
      //for (int i = 1; i < textLength; i++)
      for (int i = 0; i < textLength; i++) //MAXHALBERT
      {
         int charWrite = (int) text.charAt(i);
         writeCharToCol(i, charWrite);
      }
      //adjusts image: vertical and hortizontal 
      adjustImage();

      return true;

   }

   /**
    * helper method to generateImageFromText()
    * adjusts image vertically and horizontally
    * @return boolean
    */
   private boolean adjustImage()
   {
      //image adjustment to lower left corner 
      int leftCorner = image.MAX_HEIGHT - actualHeight;

      //adjusting the horizontal borders
      for (int x = 1; x < actualWidth - 1; x++)
      {
         //this.image.setPixel(x, this.image.MAX_HEIGHT - 1, true);
         image.setPixel( image.MAX_HEIGHT - 1, x, true); //MAXHALBERT
         if(x % 2 == 0)
         {
            //this.image.setPixel(x, leftCorner, true);
            image.setPixel(leftCorner, x, true); //MAXHALBERT
         }
      }
      //adjusting the vertical borders 
      for (int y = BarcodeImage.MAX_HEIGHT - 1; y >= leftCorner; --y)
      {
         //image.setPixel(0, y, true);
         image.setPixel(y, 0, true); //MAXHALBERT
      }
      return true;
   }
   
   /**
    * reads barcode image and and translates to String value
    * @return boolean
    */
   public boolean translateImageToText() 	
   {
	   //loops readCharFromCol() method to read characters	
      text = "";
      for(int i = 1 ; i < actualWidth - 1; i++) 
      {
         text += (readCharFromCol(i));
      }
      
      return false;
   }
   
   /**
    * helper method for generateImageFromText()
    * @return binary value 
    */ 
    private char readCharFromCol(int col) 
    {
       //adjusts value for new barcode lower left location
       int leftCorner = BarcodeImage.MAX_HEIGHT - actualHeight;
 
       int total = 0;
       for (int y = BarcodeImage.MAX_HEIGHT - 1; y > leftCorner; y--)
       {
          //if(this.image.getPixel(y, col))
          if(image.getPixel(y, col))     // MAXHALBERT CHANGE TO THIS
          {
             total += Math.pow(2, BarcodeImage.MAX_HEIGHT - (y + 2));
          }
         
       }
       return (char) total;
    }

   /**
    * helper method for generateImageFromText()
    * @param col
    * @param code
    * @return boolean
    */
    private boolean writeCharToCol(int col, int code)
   {
      //break apart the message into binary using repeated subtraction
      int row;
      int binaryDecomp = 128;
      while (code > 0)
      {
         if(code - binaryDecomp >= 0)
         {
            //use log on msg to calculate the row number
            row = (BarcodeImage.MAX_HEIGHT - 2) - (int)(Math.log(code) / Math.log(2));
            //this.image.setPixel(col, row, true);
            image.setPixel(row, col + 1, true); //MAXHALBERT
            code -= binaryDecomp;

         }
         binaryDecomp /= 2;
      }
      return true;
   }
   public void displayTextToConsole() 
   {
      // prints out the text string to the console.
      System.out.println(this.text);
   }
   
   /**
    * prints image to the console
    */
   public void displayImageToConsole() 
   {
      int leftCorner = BarcodeImage.MAX_HEIGHT - this.actualHeight;
      for (int y = leftCorner; y < BarcodeImage.MAX_HEIGHT; y++)
      {
         for (int x = 0; x < this.actualWidth; x++)
         {
            //if (this.image.getPixel(x, y))
            if (this.image.getPixel(y, x))     // MAXHALBERT CHANGE TO THIS
            {
               System.out.print(BLACK_CHAR);
            }
            else
            {
               System.out.print(WHITE_CHAR);
            }
         }
         System.out.println();
      }
   }

   // PRIVATE METHODS

   /**
    * Computes the width of the signal assuming it's already been shifted to
    * the lower left corner
    * @return The width of the signal.
    */
   private int computeSignalWidth() 
   {
      int counter = 0;
      for(int col = 0; col < image.MAX_WIDTH; col++)
      {
         if(image.getPixel(image.MAX_HEIGHT - 1, col))
            counter++;
      }
      return counter; 
   }
   /*
    * Computes the height of the signal assuming it's already been shifted
    * to the lower left corner.
    * @return The height of the signal
    */
   private int computeSignalHeight() 
   {
      int counter = 0;
      final int firstCol = 0;
      for(int row = 0; row < image.MAX_HEIGHT; row++)
         if(image.getPixel(row,firstCol))
            counter++;
      return counter;  
   }
   // resets to the left 
   private void cleanImage() 
   {
      moveImageToLowerLeft();
   }

   // Method to help with manipulation in cleanImage()
   private void moveImageToLowerLeft()
   {
      final int downOffset = countBlankRows();
      if(downOffset != 0)
         shiftImageDown(countBlankRows());

      final int leftOffset = countBlankColumns();
      if(leftOffset != 0)
         shiftImageLeft(countBlankColumns());
   } 
   /**
    * Counts the number of blank rows from the bottom
    * @return Int count of blank rows from the bottom.
    */
   private int countBlankRows()
   {
      boolean blankRow = false;
      int countRow = 0;
      for(int row = image.MAX_HEIGHT - 1; row >= 0; row--)
      {
         for(int col = 0; col < image.MAX_WIDTH; col++)
         {
            if(!image.getPixel(row,col))
               blankRow = true;
            if(image.getPixel(row,col))
            {
               blankRow = false;
               return countRow;
            }            
         }
         if(blankRow)
            countRow++;
      }   
      return countRow;
   }

   /**
    * Counts the number of blank columns from the left.
    * @return Int count of blank columns from the left.
    */
   private int countBlankColumns()
   {
      boolean blankCol = false;
      int countCol = 0;
      for(int col = 0; col < image.MAX_WIDTH; col++)
      {
         for(int row = 0; row < image.MAX_HEIGHT; row++)
         {
            if(!image.getPixel(row,col))
               blankCol = true;
            if(image.getPixel(row,col))
            {
               blankCol = false;
               return countCol;
            }            
         }
         if(blankCol)
            countCol++;
      }   
      return countCol;
   }

   /**
    * Shift the array to the left most column
    * @param offset
    */
   private void shiftImageDown(final int offset)
   {
      final int lastRow = image.MAX_HEIGHT - 1;
      int shiftBy = lastRow - offset;

      for(int row = lastRow; row >= 0; row--)
      {
         for(int col = 0; col < image.MAX_WIDTH; col++)
         {
            image.setPixel(row, col, image.getPixel(shiftBy,col));
            image.setPixel(shiftBy,col,false);
         }
         shiftBy--;
         if(shiftBy == 0)
            break;
      }
   }
   /**
    * Shifts the Array the bottom most row.
    * @param offset
    */
   private void shiftImageLeft(int offset)
   {
      for(int col = 0; col < image.MAX_WIDTH; col++,offset++)
      {
         for(int row = 0; row < image.MAX_HEIGHT; row++)
         {
            image.setPixel(row,col,image.getPixel(row,offset));
            image.setPixel(row,offset,false);
         }
      }
   }


  

}


