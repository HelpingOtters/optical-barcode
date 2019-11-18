
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
   public boolean generateImageFromText() {
      if(text == null || text.equals("") || 
            text.length() > BarcodeImage.MAX_WIDTH)
         return false;
         
      int valueASCII;
      
      actualWidth = text.length() + 2;
      
      for(int i = 0; i < text.length(); i++)
      {
         valueASCII = (int)text.charAt(i);
         writeCharToCol(i + 1, valueASCII);
      }
      
      return true;

   }
   
   public boolean translateImageToText() 	{
	   	
	   text = "";
      for(int i = 1 ; i < actualWidth - 1; i++) {
         text += (readCharFromCol(i));
      }
      
      return false;
   }
   
   // Use for generateImageFromText() and translateImageToText()
   private char readCharFromCol(int col) 
   {
      String numVal = "";
      for(int i = BarcodeImage.MAX_HEIGHT - actualHeight+1; i < BarcodeImage.MAX_HEIGHT - 1; i++) {
         if(image.getPixel(i, col)) {
            numVal += "1";
         }
         else {
        	 numVal += "0";
         }
      }
      return ((char)Integer.parseInt(numVal, 2));
   }

   // Use for generateImageFromText() and translateImageToText()
   private boolean writeCharToCol(int col, int code) {
      String binary = Integer.toBinaryString(code);
      image.setPixel(image.MAX_HEIGHT, col, true);

      if(col%2==0) {
         image.setPixel(image.MAX_HEIGHT-(binary.length()+1), col, true);
      }
      for(int i=0; i<binary.length(); i++) {
         if(binary.charAt(i) == '1') {
            image.setPixel((image.MAX_HEIGHT-1)-(i+1), col, true);
         } else {
            image.setPixel((image.MAX_HEIGHT-1)-(i+1), col, false);
         }
      }
      return true;
   }
   public void displayTextToConsole() 
   {
      // prints out the text string to the console.
      System.out.println(this.text);
   }
   
   public void displayImageToConsole() {
      int row, col;
      System.out.println();
      for(row = image.MAX_HEIGHT-actualHeight; row < image.MAX_HEIGHT; row++) {
         for(col = 0; col < actualWidth; col++) {
            if(image.getPixel(row, col) == true) {
               System.out.print("*");
            } else {
               System.out.print(" ");
            }
         }
         System.out.println();
      }
      System.out.println();
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

