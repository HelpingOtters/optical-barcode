
public class DataMatrix implements BarcodeIO
{
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';  

   private BarcodeImage image;
   private String text;
   private int actualWidth; // dependent on the data in the image. Can change as image changes
   private int actualHeight; // and can be computed from the "spine" of the image.

   // Default Constructor
   public DataMatrix()
   {
      /*
      constructs an empty, but non-null, image and text value.  The initial image should be all white, however, actualWidth and actualHeight should start at 0, so it won't really matter what's in this default image, in practice.  The text can be set to blank, "", or something like "undefined".
       */
      // Create a new empty image
      // Create a new empty text ""
      // actualWidth and actualHeight should start at 0
   }

   public DataMatrix(BarcodeImage image) 
   {
      // sets the image but leaves the text at its default value.  Call scan() and avoid duplication of code here.
   }

   public DataMatrix(String text) 
   {
      // sets the text but leaves the image at its default value. Call readText() and avoid duplication of code here.
   }

   public readText(String text) 
   {
      // a mutator for text.  Like the constructor;  in fact it is called by the constructor.
      // accepts a text string to be eventually encoded in an image. No translation is done here - i.e., any BarcodeImage that might be part of an implementing class is not touched, updated or defined during the reading of the text.
   }

   public scan(BarcodeImage image)
   {
      /*
      A mutator for image.  Like the constructor;  in fact it is called by the constructor.  Besides calling the clone() method of the BarcodeImage class, this method will do a couple of things including calling cleanImage() and then set the actualWidth and actualHeight.  Because scan() calls clone(), it should deal with the CloneNotSupportedException by embeddingthe clone() call within a try/catch block.  Don't attempt to hand-off the exception using a "throws" clause in the function header since that will not be compatible with the underlying BarcodeIO interface.  The catches(...) clause can have an empty body that does nothing.
      Accepts some image, represented as a BarcodeImage object to be described below, and stores a copy of this image.  Depending on the sophistication of the implementing class, the internally stored image might be an exact clone of the parameter, or a refined, cleaned and processed image.  Technically, there is no requirement that an implementing class use a BarcodeImage object internally, although we will do so.  For the basic DataMatrix option, it will be an exact clone.  Also, no translation is done here - i.e., any text string that might be part of an implementing class is not touched, updated or defined during the scan.
       */
   }

   // Accessor for actualWidth
   public int getActualWidth()
   {

   }

   // Accessor for actualHeight
   public int getActualHeight()
   {

   }

   /******************************************PERSON******2**************************************/
   public boolean generateImageFromText() 
   {
      // use readCharFromCol(int col) and WriteCharToCol(int col, int code)
      // Not technically an I/O operation, this method looks at the internal text stored in the implementing class and produces a companion BarcodeImage, internally (or an image in whatever format the implementing class uses).  After this is called, we expect the implementing object to contain a fully-defined image and text that are in agreement with each other.

   }

   public boolean translateImageToText() 
   {
      // use readCharFromCol(int col) and WriteCharToCol(int col, int code)
      // Not technically an I/O operation, this method looks at the internal image stored in the implementing class, and produces a companion text string, internally.  After this is called, we expect the implementing object to contain a fully defined image and text that are in agreement with each other.

   }

   // Use for generateImageFromText() and translateImageToText()
   private char readCharFromCol(int col) 
   {

   }
   // Use for generateImageFromText() and translateImageToText()
   private boolean WriteCharToCol(int col, int code)
   {

   }
   public void displayTextToConsole() 
   {
      // prints out the text string to the console.
   }

   public void displayImageToConsole() 
   {
      // should display only the relevant portion of the image, clipping the excess blank/white from the top and right.
      //  prints out the image to the console.  In our implementation, we will do this in the form of a dot-matrix of blanks and asterisks 
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
      /* 
      Assuming that the image is correctly situated in the lower-left corner of the larger boolean array, these methods use the "spine" of the array (left and bottom BLACK) to determine the actual size.
      */
      int counter = 0;
      int firstCol = 0;
      for(int row = 0; row < image.MAX_HEIGHT; row++)
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
   private void shiftImageDown(int offset)
   {
      int lastRow = image.MAX_HEIGHT - 1;
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


   // Optional 
   public void displayRawImage() 
   {
      /*
      Can be implemented to show the full image data including the blank top and right.  It is a useful debugging tool.
       */

   }


   // Optional 
   private void clearImage() 
   {
      // a nice utility that sets the image to white =  false.
   }

}


