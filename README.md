# OOP

Here is an image of the frequency table for my original data set (engine_data.csv)
![image](https://github.com/user-attachments/assets/45736c3b-0d08-40b6-91a2-e9e46c638c06)

# Java classes

There are 3 classes in total 

Control, PredictorGUI and PredictorModel.

The control class: 
1. Manages all of the interactions between the GUI and Predictor model class.
2. Implements action listeners so the buttons can work
3. Contains the main() so that the application can work.

The PredictorGUI class handles all the graphics of the app. It creates an interface with all of the buttons and dropdown boxes that allow a user to test the accuracy, 
train the model, make predictions on user inputs and also allows the user to add new training data to the model.

The predictor model class is the brain of the application and handles all of the work behind the scenes.

When the app starts it automatically loads the training and testing data, it first initiliases the predictor model and creates arrays to store the training and testing data and also loads the hardcoded frequency table. It loads the data from the csv files using the loadDataFromFile function.

When you train the model the calculateProbabilities function runs which loops through all the training data, counts how often each combination appears and how often the engine runs in each case, calculates the probability as time engine runs / total occurances and stores the results for future predictions.

When you make a prediction, it uses the PredictionResults function which basically just checks if the current combinations probability exceeds or falls short of 50%, if its above it predicts yes and if its below it predicts no.

The testing accuracy functionality works with the use of the calculateAccuracy function which makes a prediction for each test case, compares it with what actually happened, counts how many predictions were correct and calculates it as a percentage.

Lastly the functionality of adding new data works by adding the new data to the training set and immediately updates all of the probabilities so that when a user clicks train model they are able to see a frequency table with all of the new probabilities.


# List of functionality

The total list of functionality would be: 
Allows users to make predictions based on their inputs.
Data management in the forms of loading data from the programs directory, real time counts of training and testing data and allows users to input new data.
Model training which gets automatically updated with new inputs, shows users frequency table with real time probabilities, and calculates probablities.
Accuracy testing
Nice interface

# Things I wish I could've added

Some things I wish I had more time to add was a load data interface that would allow users to add more and more data through a file selecting window. I almost had this functionality in level 3 but it created a lot of problems for me when I was working on level 4. I wanted the users to be able to select files and add them to the program so that they could add files that would automatically append themselves so that the model could be trained on a lot of data and be able to test it accurately.

I also wish that the test accuracy button worked a bit differently, I wanted it to be able to test the accuracy of predictions in real time, I tried allowing the user to add data to either training or testing but it didn't really work how I envisioned it and I found it a bit too complicated to really implement. The way the model works now only tests the accuracy of predictions against the pre-loaded testing data. Ideally the way it would work is by having a more dynamic system that could evaluate predictions as they're made and maintain a running accuracy score. 

Apart from these 2 things, I think the program works quite well and meets the criteria needed so I'm quite happy with my result.


