library(RTextTools)
setwd("C:/Users/anand/Desktop/Machine Learning/assignment4/20news-bydate/")

#Reading training and test data
r_train<-read_data("train_data",type = "folder",index = "Training_label.csv", warn=F)
r_test<-read_data("test_data",type = "folder",index = "Testing_label.csv", warn=F)

#Number of rows in training and test data 
train_rows <- nrow(r_train)
test_rows <- nrow(r_test)

#Binding training and test data
full_data <- rbind(r_train, r_test)
full_rows <- nrow(full_data)

#index for seperating training and test row in the data
j = train_rows + 1

#creating a matrix for the data
doc_matrix <- create_matrix(full_data$Text.Data, language="english", removeNumbers=TRUE, stemWords=TRUE, removeSparseTerms=.998)

#creating a container and specifying training and test data
container <- create_container(doc_matrix, full_data$Labels, trainSize=1:train_rows, testSize=j:full_rows, virgin=FALSE)

SVM <- train_model(container,"SVM")
GLMNET <- train_model(container,"GLMNET")
MAXENT <- train_model(container,"MAXENT")
BOOSTING <- train_model(container,"BOOSTING")
#Since Bagging and RF involves more space (Commenting it) - Can be uncommented if we have more memory
#BAGGING <- train_model(container,"BAGGING")
#RF <- train_model(container,"RF")
TREE <- train_model(container,"TREE")
SVM_CLASSIFY <- classify_model(container, SVM)
GLMNET_CLASSIFY <- classify_model(container, GLMNET)
MAXENT_CLASSIFY <- classify_model(container, MAXENT)
BOOSTING_CLASSIFY <- classify_model(container, BOOSTING)
#BAGGING_CLASSIFY <- classify_model(container, BAGGING)
#RF_CLASSIFY <- classify_model(container, RF)
TREE_CLASSIFY <- classify_model(container, TREE)
analytics <- create_analytics(container, cbind(SVM_CLASSIFY, BOOSTING_CLASSIFY, GLMNET_CLASSIFY, TREE_CLASSIFY, MAXENT_CLASSIFY))
summary(analytics)