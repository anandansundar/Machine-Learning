#install the below packages if needed
library(ggvis)
library(e1071)
library(class)
library(randomForest)
library(rpart)
library(ipred)
library(mlbench)
library(gbm)
library(caret)
library(adabag)

#Enter the path of the files which are kept
#Note : All the files are in the zip folder which is submitted
dataDirectory <- ("C:/Users/anand/Desktop/Machine Learning/Assignment 5/datasets/")

normalize <- function(x){
  return((x-min(x))/(max(x)-min(x)))
}

#List for storing data
org_data <- list()
data <- list()

org_data[[1]] <- data.frame(read.csv(paste(dataDirectory, 'processed.cleveland.data.csv', sep=""), header = TRUE))
org_data[[2]] <- data.frame(read.csv(paste(dataDirectory, 'pima-indians-diabetes.data.csv', sep=""), header = TRUE))
org_data[[3]] <- data.frame(read.csv(paste(dataDirectory, 'transfusion.data.csv', sep=""), header = TRUE))
org_data[[4]] <- data.frame(read.csv(paste(dataDirectory, 'car.data.csv', sep=""), header = TRUE))
org_data[[5]] <- data.frame(read.csv(paste(dataDirectory, 'bupa.data.csv', sep=""), header = TRUE))

# We store the number of datasets in n
n = length(org_data)
cat("The number of datasets are" ,n)

#normalizing all the columns to the range 0 and 1 for all attributes except class
for (z in 1:n){
  col <- ncol(org_data[[z]])
  data[[z]]<- as.data.frame(lapply(org_data[[z]][,-col],normalize))
  data[[z]] <- as.data.frame(cbind(data[[z]], org_data[[z]][,col]))
  colnames(data[[z]])[col] <- "class_atr"
}

#List for storing accuracies
knn_accuracy <- list()
bag_accuracy <- list()
adaboost_accuracy <- list()
graboost_accuracy <- list()
rf_accuracy <- list()

#For n-fold cross validation
train_control <- trainControl(method="cv", number=10)


for (i in 1:n){
  
  col <- ncol(data[[i]])
  data[[i]]$class_atr <- as.factor(data[[i]]$class_atr)
  
  #KNN Algorithm
  train_knn <- train(class_atr~ ., data=data[[i]], method="knn", trControl=train_control)
  knn_accuracy[[i]] <-  100*max((train_knn$results$Accuracy))    
  
  #Random Forest Algorithm
  train_rf <- train(class_atr~ ., data=data[[i]], method="rf", trControl=train_control)
  rf_accuracy[[i]] <-  100*max((train_rf$results$Accuracy))    
  
  #Bagging Algorithm
  train_bag <- train(class_atr~ ., data=data[[i]], method="treebag", trControl=train_control)
  bag_accuracy[[i]] <-  100*(train_bag$results$Accuracy)    
  
  #Gradient Boosting Algorithm
  train_grad <- train(class_atr~ ., data=data[[i]], method="gbm", trControl=train_control)
  graboost_accuracy[[i]] <- 100*(max(train_grad$results$Accuracy))
  
  #Adaboosting Algorithm
  Grid <- expand.grid(maxdepth=25,mfinal=10, coeflearn="Breiman")
  train_ada <- train(class_atr~ ., data=data[[i]], method = "AdaBoost.M1", trControl = train_control, tuneGrid=Grid)
  adaboost_accuracy[[i]] <- 100*(train_ada$results$Accuracy)
  
}

#output can be seen by typing below on the R console
#knn_accuracy
#bag_accuracy
#rf_accuracy
#adaboost_accuracy
#graboost_accuracy

