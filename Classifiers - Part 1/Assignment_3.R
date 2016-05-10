#install them if you don't have them
require(rpart)
require(rpart.plot)
require(e1071)
require(nnet)

#Enter the path of the files which are kept
#Note : All the files are in the zip folder which is submitted
dataDirectory <- ("C:/Users/anand/Desktop/Machine Learning/Assignment_3/datasets/")

#List for storing data
data <- list()

data[[1]] <- data.frame(read.csv(paste(dataDirectory, 'processed.cleveland.data.csv', sep=""), header = TRUE))
data[[2]] <- data.frame(read.csv(paste(dataDirectory, 'pima-indians-diabetes.data.csv', sep=""), header = TRUE))
data[[3]] <- data.frame(read.csv(paste(dataDirectory, 'transfusion.data.csv', sep=""), header = TRUE))
data[[4]] <- data.frame(read.csv(paste(dataDirectory, 'car.data.csv', sep=""), header = TRUE))
data[[5]] <- data.frame(read.csv(paste(dataDirectory, 'bupa.data.csv', sep=""), header = TRUE))

#Matrix for displaying accuracies for classifiers
#dtree - Decision Tree
#svm - Support Vector Machines
#nb - Naive Bayes
#nn - Artificial Neural Network
#perceptron - Perceptron

dtree_accuracy <- matrix(data = NA, nrow = 5, ncol = 2)
svm_accuracy <- matrix(data = NA, nrow = 5, ncol = 2)
nb_accuracy <- matrix(data = NA, nrow = 5, ncol = 2)
nn_accuracy <- matrix(data = NA, nrow = 5, ncol = 2)
perceptron_accuracy <- matrix(data = NA, nrow = 5, ncol = 2)

#Here n is 5
n = length(data)
print(n)

for (i in 1:n){
  #data[[i]]$class_atr <- factor(data[[i]]$class_atr)
  for (j in 1:2){
    
    col <- ncol(data[[i]])
    
    #Percent Split - 80/20
    sample_set <- sort(sample(1:nrow(data[[i]]), round(nrow(data[[i]]) * 0.8)))
    train_set <- data[[i]][sample_set, ]
    test_set <- data[[i]][-sample_set, ]
    
    #Perceptron
    train_perceptron <- nnet(factor(class_atr)~., train_set, size=0, maxit=1000, na.action = na.omit, skip = TRUE)
    prediction_perceptron <- predict(train_perceptron, test_set[,-col], type = "class")
    perceptron_accuracy[i, j] <- (mean(prediction_perceptron == test_set[col]))*100
    
    #Artificial Neural Network
    train_nn <- nnet(factor(class_atr)~., train_set, size=3, maxit=1000, na.action = na.omit, skip = FALSE)
    prediction_nn <- predict(train_nn, test_set[,-col], type = "class")
    nn_accuracy[i, j] <- (mean(prediction_nn == test_set[col]))*100
    
    #Decision Tree
    train_dtree <- rpart(factor(class_atr) ~ ., data = train_set, method = 'class', parms = list(split ='information'))
    prediction_dtree <- predict(train_dtree, test_set, type = "class")
    dtree_accuracy[i, j] <- 100*sum(prediction_dtree == test_set$class_atr)/length(prediction_dtree)
    
    #Support Vector Machine
    train_svm <- svm(factor(class_atr) ~ ., data = train_set, scale = FALSE)
    prediction_svm <- predict(train_svm, test_set, type = "raw")
    svm_accuracy[i, j] <- 100*sum(prediction_svm == test_set$class_atr)/length(prediction_svm)
    
    #Naive Bayes
    train_nb <- naiveBayes(factor(class_atr) ~., data = train_set)
    prediction_nb <- predict(train_nb, test_set)
    nb_accuracy[i, j] <- 100*sum(prediction_nb == test_set$class_atr)/length(prediction_nb)
   
    
  }
}

#output can be seen by typing below on the R console
#view(dtree_accuracy)
#view(svm_accuracy) 
#view(nb_accuracy) 
#view(nn_accuracy) 
#view(perceptron_accuracy) 