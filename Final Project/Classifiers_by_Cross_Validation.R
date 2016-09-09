#Note : This program will take 45 minutes to run approximately
#R program for classifying data from Airbnb dataset (k - fold cross validation)
#Install the below packages.
#Also, install dependent packages whenever R prompts.
library(rpart)
library(e1071)
library(nnet)
library(adabag)
library(randomForest)
library(caret)
library(adabag)
library(plyr)
library(kernlab)

#Enter the path of the files
#Note : All the files are in the zip folder which is submitted
dataDirectory <- ("C:\\Users\\anand\\Desktop\\Machine Learning\\ML_Project\\Dataset\\")

#Training Data from airbnb which is preprocessed by us
airbnb_data <- read.csv(paste(dataDirectory, 'airbnb_dataset.csv', sep=""), header = TRUE)

#We are making sure that the attribute to be predicted is a factor type.
airbnb_data$country_destination <- as.factor(airbnb_data$country_destination)

#We are performing k fold cross validation - let k = 3
Training_rule <- trainControl(method="cv", number=3, returnResamp = "all", classProbs = TRUE)


#Finding accuracy and kappa for Decision Tree with cross validation
train_dtree <- train(country_destination ~ ., data = airbnb_data, method = "rpart", tuneLength = 9, trControl=Training_rule)
accr_dtree_cv <-  100*max((train_dtree$results$Accuracy))
kappa_dtree_cv <- 100*max((train_dtree$results$Kappa))

#Visualize to find the best complexity parameter that algorithm takes
#Run the below command on the console
#plot(train_dtree)

#Finding accuracy and kappa for Neural Network with cross validation
train_nnet <- train(country_destination ~., data = airbnb_data, method = "nnet", trControl = Training_rule, preProc = c("center", "scale"), trace = FALSE)
accr_nnet_cv <- 100*max((train_nnet$results$Accuracy))
kappa_nnet_cv <- 100*max((train_nnet$results$Kappa))

#Visualize to find the accuracy and weight decay of neural network
#Run the below command on the console
#plot(train_nnet)

#Finding accuracy and kappa for SVM with radial kernel with cross validation
Grid_svm <- data.frame(.C = c(.25, .5, 1), .sigma = .05)
train_svm <- train(country_destination~ ., data=airbnb_data, method="svmRadial", trControl=Training_rule, tuneGrid=Grid_svm, preProc= c("center", "scale"))
accr_svm_cv <-  100*max((train_svm$results$Accuracy))
kappa_svm_cv <- 100*max((train_svm$results$Kappa))

#Visualize to find the cost and accuracy of support vector machines
#Run the below command on the console
#plot(train_svm)

#Finding accuracy and kappa for Random Forest with cross validation
train_rf <- train(country_destination~ ., data=airbnb_data, method="rf", trControl=Training_rule)
accr_rf_cv <-  100*max((train_rf$results$Accuracy))    
kappa_rf_cv <- 100*max((train_rf$results$Kappa))

#Visualize to find the accuracy and randomly selected predictors for random forests
#Run the below command on the console
#plot(train_rf)

#Finding accuracy and kappa for Bagging with cross validation
train_bag <- train(country_destination~ ., data=airbnb_data, method="treebag", trControl=Training_rule, nbagg = 7, keepX = TRUE)
accr_bag_cv <-  100*(train_bag$results$Accuracy)    
kappa_bag_cv <- 100*(train_bag$results$Kappa)

#Finding accuracy and kappa for Gradient Boosting with cross validation
train_gboost <- train(country_destination~ ., data=airbnb_data, method="gbm", trControl=Training_rule)
accr_gboost_cv <- 100*(max(train_gboost$results$Accuracy))
kappa_gboost_cv <- 100*(max(train_gboost$results$Kappa))

#Finding accuracy and kappa for Ada Boosting with cross validation
Grid_aboost <- expand.grid(maxdepth=25,mfinal=10, coeflearn="Breiman")
train_aboost <- train(country_destination~ ., data=airbnb_data, method = "AdaBoost.M1", trControl = Training_rule, tuneGrid=Grid_aboost)
accr_aboost_cv <- 100*(train_aboost$results$Accuracy)
kappa_aboost_cv <- 100*(train_aboost$results$Kappa)

#See accuracy and kappa values in

#accr_aboost_cv
#kappa_aboost_cv
#accr_gboost_cv
#kappa_gboost_cv
#accr_bag_cv
#kappa_bag_cv
#accr_rf_cv
#kappa_rf_cv
#accr_svm_cv
#kappa_svm_cv
#accr_nnet_cv
#kappa_nnet_cv
#accr_dtree_cv
#kappa_dtree_cv