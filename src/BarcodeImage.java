package src;
/**
 * It stores a barcode image as boolean type 2D array and allows retrieve and
 * change of data in the array. It implements Clonable interface because it
 * contains deep data.
 * 
 * @author Max Halbert
 * @version November 18, 2019 The structure for creating a BarcodeImage
 */

public class BarcodeImage implements Cloneable
{
   public static final int MAX_HEIGHT = 30; // maximum number of rows
   public static final int MAX_WIDTH = 65; // maximum number of columns

   private boolean[][] imageData; // 2D array to store data of a barcode image

   /**
    * Default constructor only create array with blank image data
    */
   public BarcodeImage()
   {
      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
      initImageData();
   }

   /**
    * Constructor that takes an array of String that contain image data then
    * convert and store them in the 2D array
    * 
    * @param strData array of String that contain image data
    */
   public BarcodeImage(String[] strData)
   {
      // create and initialize the 2D array, get ready for the image
      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
      initImageData();

      if (!checkSize(strData))
      {
         // the input image data is too big for the 2D array to hold
         System.out.println("Fatal error, import string data is too big!");
         System.exit(0);
      }

      // string data starts from the last string and imageData starts from last
      // row
      // loop until the first string is taken care of
      for (int row = MAX_HEIGHT - 1, s = strData.length - 1; s >= 0; row--, s--)
      {
         // starts from the left then to the right in the string and the array
         for (int col = 0, chCtr = 0; chCtr < strData[s]
            .length(); col++, chCtr++)
         {
            if (strData[s].charAt(chCtr) == '*')
            {
               // '*' means black in the image and will be stored as true value
               imageData[row][col] = true;
            }
            else
            {
               // space means white in the image and will be stored as false
               // value
               imageData[row][col] = false;
            }
         }
      }

   }

   /**
    * Copy Constructor for the clone method since it contains 2D array that
    * needs deep copy
    * 
    * @param other this object copies all the data from other
    */
   public BarcodeImage(BarcodeImage other)
   {
      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH]; // create a new 2D array

      // Copy all the data from other object
      for (int row = 0; row < MAX_HEIGHT; row++)
      {
         for (int col = 0; col < MAX_WIDTH; col++)
         {
            imageData[row][col] = other.getPixel(row, col);
         }
      }
   }

   /**
    * Check the size of data whether can fit into the 2D array
    * 
    * @param data the array of strings that contain image data
    * @return true if the size of data fits into the 2D array
    * @return false if the size is too big to store or null data
    */
   private boolean checkSize(String[] data)
   {
      if (data == null)
      {
         return false; // data is null
      }
      if (data.length <= MAX_HEIGHT)
      {
         for (int s = 0; s < data.length; s++)
         {
            if (data[s].length() > MAX_WIDTH)
            {
               return false;
            }
         }
         return true; // it fits
      }
      return false; // too big
   }

   /**
    * initialize the 2D imageData with false value than means white background
    */
   private void initImageData()
   {
      for (int row = 0; row < MAX_HEIGHT; row++)
      {
         for (int col = 0; col < MAX_WIDTH; col++)
         {
            imageData[row][col] = false;
         }
      }
   }

   /**
    * Accessor to return the value of a pixel in imageData
    * 
    * @param row index to which row in the imageData
    * @param col index to which column in the imageData
    * @return true or false
    */
   public boolean getPixel(int row, int col)
   {
      if (row < MAX_HEIGHT && col < MAX_WIDTH)
      {
         return imageData[row][col];
      }
      return false;
   }

   /**
    * Mutator to change the value of a pixel in imageData
    * 
    * @param row   index to which row in the imageData
    * @param col   index to which column in the imageData
    * @param value the value to be set in the pixel
    * @return true if successfully changed otherwise false
    */
   public boolean setPixel(int row, int col, boolean value)
   {
      if (row < MAX_HEIGHT && col < MAX_WIDTH)
      {
         // within bound
         imageData[row][col] = value;
         return true;
      }
      return false; // out of bound
   }

   /**
    * Override the Object.clone() before there is 2D array in the data which
    * requires deep copy - cannot be done by Object.clone().
    * 
    * @return a copy of the current object
    */
   @Override
   public BarcodeImage clone() throws CloneNotSupportedException
   {
      // use the copy constructor to do the deep copy of the object
      BarcodeImage image = new BarcodeImage(this);
      return image;
   }

   /**
    * This is for testing purpose to display the data in the imageData
    */
   public void displayToConsole()
   {
      for (int col = 0; col < MAX_WIDTH + 2; col++)
      {
         System.out.print('-');
      }
      System.out.println();
      for (int row = 0; row < MAX_HEIGHT; row++)
      {
         System.out.print('|');
         for (int col = 0; col < MAX_WIDTH; col++)
         {
            if (imageData[row][col])
            {
               System.out.print('*');
            }
            else
            {
               System.out.print(' ');
            }
         }
         System.out.print("|\n");
      }
      for (int col = 0; col < MAX_WIDTH + 2; col++)
      {
         System.out.print('-');
      }
      System.out.println();
   }
}
