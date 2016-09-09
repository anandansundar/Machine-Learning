#R program for classifying data from Airbnb dataset (80 - 20 Split)
#Install the below packages.
#Also, install dependent packages whenever R prompts.

library(rpart)
library(e1071)
library(nnet)
library(adabag)
library(randomForest)
library(plyr)
library(rpart.plot)

#Enter the path of the files which are kept
#Note : All the files are in the zip folder which is submitted
dataDirectory <- ("C:\\Users\\anand\\Desktop\\Machine Learning\\ML_Project\\Dataset\\")

#Training Data from airbnb and preprosessed by us
airbnb_data <- data.frame(read.csv(paste(dataDirectory, 'airbnb_dataset.csv', sep=""), header = TRUE))

#We are making sure that the attribute to be predicted is a factor type.
airbnb_data$country_destination <- as.factor(airbnb_data$country_destination)

#Splitting the data - 80% for training and 20% for testing
sample_set <- sample(nrow(airbnb_data), floor(nrow(airbnb_data) * 0.8))
train_set <- airbnb_data[sample_set, ]
test_set <- airbnb_data[-sample_set, ]

#Finding accuracy for Decision Tree with 80 - 20 split 
train_dtree <- rpart(country_destination ~ ., data = train_set, method = 'class', parms = list(split ='information'))
pruned_dtree <- prune(train_dtree, cp = 0.01)
prediction_dtree <- predict(pruned_dtree, test_set, type = "class")
dtree_accuracy <- 100*sum(prediction_dtree == test_set$country_destination)/length(prediction_dtree)

#Plotting the decision tree
#rpart.plot(pruned_dtree)

#Finding accuracy for Perceptron with 80 - 20 split
train_perceptron <- nnet(country_destination~., train_set, size=0, maxit=1000, na.action = na.omit, skip = T , MaxNWts = 30000)
prediction_perceptron <- predict(train_perceptron, test_set, type = "class")
nn_accuracy <- 100*sum(prediction_perceptron == test_set$country_destination)/length(prediction_perceptron)


#Finding accuracy for Neural networks with hidden layers with 80 - 20 split
train_nn <- nnet(country_destination~. , train_set, size = 20, maxit = 1000, na.action = na.omit , skip = F , MaxNWts = 30000)
prediction_nn <- predict(train_nn, test_set , type = "class")
ann_accuracy <- 100*sum(prediction_nn == test_set$country_destination)/length(prediction_nn)


#Finding accuracy for Support Vector Machine with 80 - 20 split
train_svm <- svm(country_destination~., data = train_set, scale = FALSE , kernel = "polynomial")
prediction_svm <- predict(train_svm, test_set, type = C)
svm_accuracy <- 100*sum(prediction_svm == test_set$country_destination)/length(prediction_svm)


#Finding accuracy for Naive Bayes with 80 - 20 split
train_nb <- naiveBayes(country_destination~., data = train_set)
prediction_nb <- predict(train_nb, test_set)
nb_accuracy <- 100*sum(prediction_nb == test_set$country_destination)/length(prediction_nb)


#Finding accuracy for Bagging with 80 - 20 split
train_bag <- bagging(country_destination~.,data = train_set, mfinal = 10)
prediction_bag <- predict(train_bag , test_set)
bagging_accuracy <- 100 - (prediction_bag$error)*100


#Finding accuracy for Random Forest with 80 - 20 split
train_rf <- randomForest(country_destination~., data=train_set, importance = TRUE)
prediction_rf <- predict(train_rf , test_set)
rf_accuracy <- 100*sum(prediction_rf == test_set$country_destination)/length(prediction_rf)


#Finding accuracy for Ada Boosting with 80 - 20 split
train_adaboost <- boosting(country_destination~., data=train_set)
prediction_adaboost <- predict.boosting(train_adaboost , test_set)
adaboost_accuracy <- 100 - (prediction_adaboost$error)*100

#See all _accuracy variables for accuracies 