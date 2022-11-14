## Processor


### This is an application created for processing xml files and writing new xml files using STAX library. The container folder is created for testing purposes for storing the order files before adding them to input directory.
### The xml files that are added in the input folder are seen and processed if the filename follows the pattern "orders##.xml". 
### After creating  OrderInformation, Order and Product objects from the xml, the objects are used to create xml files grouped by on supplier, ordered by product price and order creation date. The files resulted are placed in the output folder.
### The application is prepared to handle exceptions and is validated.  
### Test classes ascertain that the application executes as expected.

## Technologies used:

<p>
<img src="https://user-images.githubusercontent.com/89586309/201597086-1a670837-e7e4-4366-b945-e476d3e66bff.png"  width="40" alt="junit">
<img src="https://user-images.githubusercontent.com/89586309/201597350-c2498c6d-75d0-4bf0-92bd-b269727c6ff3.png"  width="70" alt="java">
<img src="https://user-images.githubusercontent.com/89586309/201597522-5b02a196-1702-4ac7-be41-7a62076a2858.png"  width="100" alt="maven">
</p>