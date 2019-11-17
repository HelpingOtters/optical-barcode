/**
 * 
 * @author Max Halbert
 * @version November 14, 2019
 * The structure for creating a BarcodeImage
 */
public class BarcodeImage implements Cloneable
{
   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;
   
   private boolean[][] imageData;
   
   //default constructor
   public BarcodeImage()
   {
      initImageData();
   }
   
   public BarcodeImage(String[] strData)
   {
      initImageData();
      
      if(!checkSize(strData))
      {
         System.out.println("Fatal error, import string data is too big!");
         System.exit(0);
      }
      
      // string data starts from the last string but imageData starts from first row
      // loop until the first string is taken care of 
      for(int row = MAX_HEIGHT - 1, s = strData.length - 1; s >= 0; row--, s--)
      {
         // starts from the left then to the right in the string and the array 
         for(int col = 0, chCtr = 0; chCtr < strData[s].length(); col++, chCtr++)
         {
            if(strData[s].charAt(chCtr) == '*')
            {
               imageData[row][col] = true;
            }
            else
            {
               imageData[row][col] = false;
            }
         }
      }
      
   }
   
   // Copy constructor
   public BarcodeImage(BarcodeImage other)
   {
      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
      for(int row = 0; row < MAX_HEIGHT; row++)
      {
         for(int col = 0; col < MAX_WIDTH; col++)
         {
            imageData[row][col] = other.getPixel(row, col);
         }
      }
   }
   
   private boolean checkSize(String[] data)
   {
      if(data == null)
      {
         return false;
      }
      if(data.length <= MAX_HEIGHT)
      {
         for(int s = 0; s < data.length; s++)
         {
            if(data[s].length() > MAX_WIDTH)
            {
               return false;
            }
         }
         return true;  
      }
      return false;
   }
   
   private void initImageData()
   {
      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
      for(int row = 0; row < MAX_HEIGHT; row++)
      {
         for(int col = 0; col < MAX_WIDTH; col++)
         {
            imageData[row][col] = false;
         }
      }
   }
   
   // Accessor
   public boolean getPixel(int row, int col)
   {
      if(row < MAX_HEIGHT && col < MAX_WIDTH)
      {
         return imageData[row][col];
      }
      return false;
   }
   
   // Mutator
   public boolean setPixel(int row, int col, boolean value)
   {
      if(row < MAX_HEIGHT && col < MAX_WIDTH)
      {
         imageData[row][col] = value;
         return true;
      }
      return false;
   }
   
   public BarcodeImage clone()
   {
      return new BarcodeImage(this);
   }
   
   public void displayToConsole()
   {
      for(int col = 0; col < MAX_WIDTH +2; col++)
      {
         System.out.print('-');
      }
      System.out.println();
      for(int row = 0; row < MAX_HEIGHT; row++)
      {
         System.out.print('|');
         for(int col = 0; col < MAX_WIDTH; col++)
         {
            if(imageData[row][col])
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
      for(int col = 0; col < MAX_WIDTH +2; col++)
      {
         System.out.print('-');
      }
      System.out.println();
   }
}
