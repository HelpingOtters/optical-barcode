package src;
public class DataMatrix implements BarcodeIO
{
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';  

   private BarcodeImage image;
   private String text;
   private int actualWidth; // dependent on the data in the image. Can change as image changes
   private int actualHeight; // and can be computed from the "spine" of the image.

   /****************************************** PERSON 1 ****************************************/
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
   public DataMatrix(BarcodeImage image) 
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
   public DataMatrix(String text) 
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
   public boolean readText(String text) 
   {
      if(text == null)
         return false;

      this.text = text;
      return true;
   }

   /**
    * Method to read in the image, make a copy of it and clean it up
    * @param image
    */
   public boolean scan(BarcodeImage image) 
   {
      
      try {
         this.image = image.clone();
      } catch (CloneNotSupportedException e) {
         System.out.println("Clone error!");
         return false;
      }
   
      cleanImage();
      actualWidth = computeSignalWidth(); 
      actualHeight = computeSignalHeight();
      return true;
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
   /**************************************** END OF PERSON 1 ************************************/

   /******************************************PERSON******2**************************************/
   public boolean generateImageFromText() 
   {
      /**
       * use readCharFromCol(int col) and WriteCharToCol(int col, int code)
       * Not technically an I/O operation, this method looks at the internal text stored in the 
       * implementing class and produces a companion BarcodeImage, internally 
       * (or an image in whatever format the implementing class uses).  After this is called, 
       * we expect the implementing object to contain a fully-defined image and text that are in 
       * agreement with each other.
       */ 
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

   public boolean translateImageToText() 
   {
      /**
       * use readCharFromCol(int col) and WriteCharToCol(int col, int code)
       * Not technically an I/O operation, this method looks at the internal text stored in the 
       * implementing class and produces a companion BarcodeImage, internally 
       * (or an image in whatever format the implementing class uses).  After this is called, 
       * we expect the implementing object to contain a fully-defined image and text that are in 
       * agreement with each other.
       */ 
      text = "";
      for (int x = 1; x < actualWidth - 1; x++)
      {
         text += readCharFromCol(x);
      }
      return true;

   }

   // Use for generateImageFromText() and translateImageToText()
   private char readCharFromCol(int col) 
   {
      //adjusts value for new barcode lower left location
      int leftCorner = BarcodeImage.MAX_HEIGHT - actualHeight;

      int total = 0;
      for (int y = BarcodeImage.MAX_HEIGHT - 1; y > leftCorner; y--)
      {
         //if(this.image.getPixel(col, y))
         if(image.getPixel(y, col))     // MAXHALBERT CHANGE TO THIS
         {
            total += Math.pow(2, BarcodeImage.MAX_HEIGHT - (y + 2));
         }
        
      }
      return (char) total;

   }

   // Use for generateImageFromText() and translateImageToText()
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

   public void displayImageToConsole() 
   {
      /**
       * should display only the relevant portion of the image, 
       * clipping the excess blank/white from the top and right.
       * prints out the image to the console.  
       * In our implementation, we will do this in the form of a dot-matrix 
       * of blanks and asterisks
       */

      //displays data 
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

   /****************************************END*****OF*******PERSON2************************************/

   // PRIVATE METHODS

   /**
    * Computes the width of the signal assuming it's already been shifted to
    * the lower left corner
    * @return The width of the signal.
    */
   private int computeSignalWidth() 
   {
      /* 
       Assuming that the image is correctly situated in the lower-left corner of the larger boolean array, these methods use the "spine" of the array (left and bottom BLACK) to determine the actual size.
       */
      int counter = 0;
      for(int col = 0; col < BarcodeImage.MAX_WIDTH; col++)
      {
         if(image.getPixel(BarcodeImage.MAX_HEIGHT - 1, col))
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
      /* 
       Assuming that the image is correctly situated in the lower-left corner of the larger boolean array, these methods use the "spine" of the array (left and bottom BLACK) to determine the actual size.
       */
      int counter = 0;
      int firstCol = 0;
      for(int row = 0; row < BarcodeImage.MAX_HEIGHT; row++)
         if(image.getPixel(row,firstCol))
            counter++;
      return counter;  
   }

   private void cleanImage() 
   {
      /*
       This private method will make no assumption about the placement of the "signal" within a passed-in BarcodeImage.  In other words, the in-coming BarcodeImage may not be lower-left justified. 

       The cleanImage() method would be called from within scan() and would move the signal to the lower-left of the larger 2D array.  And, since scan() is called by the constructor, that implies that the image gets adjusted upon construction.  This kind of standardization represents the many other image processing tasks that would be implemented in the scan() method.  Error correction would be done at this point in a real class design. 
       */
      moveImageToLowerLeft();
   }

   // Method to help with manipulation in cleanImage()
   private void moveImageToLowerLeft()
   {
      int downOffset = countBlankRows();
      if(downOffset != 0)
         shiftImageDown(countBlankRows());

      int leftOffset = countBlankColumns();
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
      for(int row = BarcodeImage.MAX_HEIGHT - 1; row >= 0; row--)
      {
         for(int col = 0; col < BarcodeImage.MAX_WIDTH; col++)
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
      for(int col = 0; col < BarcodeImage.MAX_WIDTH; col++)
      {
         for(int row = 0; row < BarcodeImage.MAX_HEIGHT; row++)
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
   private void shiftImageDown(int offset)
   {
      int lastRow = BarcodeImage.MAX_HEIGHT - 1;
      int shiftBy = lastRow - offset;

      for(int row = lastRow; row >= 0; row--)
      {
         for(int col = 0; col < BarcodeImage.MAX_WIDTH; col++)
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
      for(int col = 0; col < BarcodeImage.MAX_WIDTH; col++,offset++)
      {
         for(int row = 0; row < BarcodeImage.MAX_HEIGHT; row++)
         {
            image.setPixel(row,col,image.getPixel(row,offset));
            image.setPixel(row,offset,false);
         }
      }
   }


   // Optional 
   public void displayRawImage() 
   {
      /*
       Can be implemented to show the full image data including the blank top and right.  It is a useful debugging tool.
       */
      /*
      Can be implemented to show the full image data including the blank top and right.  It is a useful debugging tool.
      */
     for(int row = 0; row < BarcodeImage.MAX_HEIGHT; row++)
     {
        System.out.print("|");
        for(int col = 0; col < BarcodeImage.MAX_WIDTH; col++)
        {
           if(image.getPixel(row, col))
           {
              System.out.print(BLACK_CHAR);
           }
           else
           {
              System.out.print(WHITE_CHAR);
           }
        }
        System.out.print("|");
        System.out.println();
         
     }

   }


   // Optional 
   private void clearImage() 
   {
      // a nice utility that sets the image to white =  false.
   }

}


