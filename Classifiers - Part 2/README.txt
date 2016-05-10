Source the R program Assignment_5.R
If you don't have the packages installed, kindly install it using install.packages()

Please make sure that the correct path of the datasets has been given in line #15 of Assignment5.R

Enter the path of the datasets here:

For example line #15 looks like,
dataDirectory <- ("C:/Users/anand/Desktop/Machine Learning/Assignment 5/datasets/")

#For n-fold cross validation
We use 10 - fold cross validation here
train_control <- trainControl(method="cv", number=10)

output can be seen by typing below on the R console

knn_accuracy
bag_accuracy
rf_accuracy
adaboost_accuracy
graboost_accuracy

Look into sample_output.txt for reference.