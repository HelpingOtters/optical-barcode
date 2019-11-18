package src;
/**
 * Phase 1
 * 
 * BarcodeIO.java
 * 
 * @author Ricardo Barbosa 
 * @version November 14, 2019
 * 
 * Outlines the operations for BarcodeImages
 * 
 */

public interface BarcodeIO
{
   //stores a copy of the image 
   public boolean scan(BarcodeImage bc);

   //stores a string to be encoded into BarcodeImage
   public boolean readText(String text);

   //analyzes internal text to be encoded into BarcodeImage
   public boolean generateImageFromText();

   //analyzes internal BarcodeImage to decode to text 
   public boolean translateImageToText();

   //prints string to console
   public void displayTextToConsole();

   //prints dot matrix to the console
   public void displayImageToConsole();

}
