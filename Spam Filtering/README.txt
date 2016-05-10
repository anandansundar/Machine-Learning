Run the R file Assignment_4.R

In the program, line #2 setwd("C:/Users/anand/Desktop/Machine Learning/assignment4/20news-bydate/")
was done to set the directory. This has to be changed accordingly.
Training and Testing label CSV files also has to be placed in the same directory.

The following testing and training datasets were considered for this assignment from the 20news-bydate dataset.
alt.atheism
comp_graphics 
rec.autos
sci.crypt
talk.politics.guns

Testing and Training data were taken in the above topics for the given dataset and label files are generated.
Training data indices can be found in Training_label.csv and Testing data will be found in Testing_label.csv.

All possible classifiers are computed barring RF and Bagging.

Since memory is insufficient for Bagging and RF, they are commented in the R script.

The script will take around 20 mins to run, if we need it to be faster, please comment classifiers as needed.