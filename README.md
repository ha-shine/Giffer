# Giffer

Giffer is a simple and easy-to-use java class to make gif animation from images.

## Usage
You can either use an array of filenames like this :
```Java
Giffer.generateFromFiles(String[] filenames, String output, int delay, boolean loop)
```
Or an array of BufferedImage.
```Java
Giffer.generateFromFiles(BufferedImage[] images, String output, int delay, boolean loop)
```
<img src="sample-images/output.gif" width="150" height="150"/>
