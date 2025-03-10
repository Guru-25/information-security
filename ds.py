skikit-learn imbalanced-learn

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from collections import Counter
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler, LabelEncoder
from sklearn.ensemble import RandomForestClassifier
from sklearn.svm import SVC
from sklearn.linear_model import LogisticRegression
from sklearn.tree import DecisionTreeClassifier
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import accuracy_score, classification_report, confusion_matrix, roc_curve, auc
from imblearn.over_sampling import SMOTE
from imblearn.under_sampling import RandomUnderSampler

# Load Dataset
df = pd.read_csv("sample_data/ane.csv")

# Display basic info and first few rows
print("Dataset Info:")
df.info()
print("\nFirst 5 rows:")
print(df.head())

# Summary Statistics
print("\nSummary Statistics:")
print(df.describe())

# Check for missing values
print("\nMissing Values:")
print(df.isnull().sum())

# Handling Missing Values (Filling with Median)
df.fillna(df.median(numeric_only=True), inplace=True)

# central tendency
numeric_columns = ['Red Pixel', 'Green pixel', 'Blue pixel']
for col in numeric_columns:
  mean_val = df[col].mean()
  median_val = df[col].median()
  mode_val = df[col].mode()[0]
  print(f'{col} - Mean: {mean_val}, Median: {median_val}, Mode: {mode_val}')

# Calculate dispersion measures
numeric_columns = df.select_dtypes(include=[np.number]).columns.tolist()
for col in numeric_columns:
    std_val = df[col].std()
    var_val = df[col].var()
    range_val = df[col].max() - df[col].min()
    iqr_val = df[col].quantile(0.75) - df[col].quantile(0.25)
    print(f'{col} - Standard Deviation: {std_val}, Variance: {var_val}, Range: {range_val}, IQR: {iqr_val}')

# Compute Skewness and Kurtosis
from scipy.stats import skew, kurtosis
for col in numeric_columns:
    skew_val = skew(df[col])
    kurt_val = kurtosis(df[col])
    print(f'{col} - Skewness: {skew_val}, Kurtosis: {kurt_val}')
import matplotlib.pyplot as plt
# Histogram and Box Plot for each numeric column
for col in numeric_columns:
    plt.figure(figsize=(12, 6))
    # Histogram
    plt.subplot(1, 2, 1)
    sns.histplot(df[col], kde=True, bins=20, color='red')
    plt.title(f'Histogram of {col}')
    plt.xlabel(col)
    plt.ylabel('Frequency')
    # Box Plot
    plt.subplot(1, 2, 2)
    sns.boxplot(y=df[col], color='blue')
    plt.title(f'Box Plot of {col}')
    plt.ylabel(col)
    plt.show()

# missing
print("Initial Dataset Shape:", df.shape)
print("Missing Values Before Handling:\n", df.isnull().sum())
# Handle missing values by filling with default label
df.fillna({'Labels': 'Unknown'}, inplace=True)
# df.fillna({'Labels': 'Unknown', 'ID': 0}, inplace=True)  # 'Labels' -> 'Unknown', 'ID' -> 0
print("Missing Values After Handling:\n", df.isnull().sum())
df.describe()

# Encoding categorical variable 'Anaemic' as binary
df['Anaemic'] = df['Anaemic'].map({'Yes': 1, 'No': 0})
# df['Sex'] = df['Sex'].map({'M': 1, 'F': 0})
print(df.head())

# Drop any rows with missing values
df = df.dropna()
# Compute correlation and covariance matrices
correlation_matrix =df.corr(numeric_only=True)
covariance_matrix =df.cov(numeric_only=True)
# Print the correlation and covariance matrices
print("Correlation Matrix:\n",correlation_matrix)
print("\nCovariance Matrix:\n",covariance_matrix)

# Visualize the correlation matrix with a heatmap
plt.figure(figsize=(8,6))
sns.heatmap(correlation_matrix,annot=True, cmap="coolwarm",fmt=".2f")
plt.title("Correlation Heatmap - AnemiaDetection")
plt.show()

# plotting scatter plots
variables = ["Red Pixel", "Green pixel","Blue pixel", "Hb"]
for i in range(len(variables)):
  for j in range(i+1, len(variables)):
    plt.figure(figsize=(6,4))
    sns.scatterplot(x=df[variables[i]],y=df[variables[j]])
    plt.xlabel(variables[i])
    plt.ylabel(variables[j])
    plt.title(f"Scatter Plot: {variables[i]} vs {variables[j]}")
    plt.show()

# Selecting relevant features and target variable
X = df[["Sex", "Red Pixel", "Green pixel", "Blue pixel"]]
y = df["Anaemic"]
print(Counter(y))
print(df.head())

# Balancing Dataset
oversampler = SMOTE(random_state=42)
undersampler = RandomUnderSampler(random_state=42)

# Choose one method (oversampling or undersampling)
X_bal, y_bal = oversampler.fit_resample(X, y)  # Oversampling
# X_bal, y_bal = undersampler.fit_resample(X, y)  # Undersampling
print(Counter(y_bal))

# Split into training and testing sets (80% train, 20% test)
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Normalize the feature values
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

models = {
  "Logistic Regression": LogisticRegression(),
  "Decision Tree": DecisionTreeClassifier(),
  "Random Forest": RandomForestClassifier(),
  "SVM": SVC(probability=True),
  "KNN": KNeighborsClassifier()
}
plt.figure(figsize=(8, 6))
for name, model in models.items():
  model.fit(X_train_scaled, y_train)
  y_pred = model.predict(X_test_scaled)
  y_prob = model.predict_proba(X_test_scaled)[:, 1] if hasattr(model, "predict_proba") else None
  print(f"{name} Accuracy:", accuracy_score(y_test, y_pred))
  print("Classification Report:\n", classification_report(y_test, y_pred))
  # Confusion Matrix
  cm = confusion_matrix(y_test, y_pred)
  plt.figure(figsize=(5, 4))
  sns.heatmap(cm, annot=True, fmt='d', cmap='Blues')
  plt.xlabel('Predicted')
  plt.ylabel('Actual')
  plt.title(f'Confusion Matrix - {name}')
  plt.show()

  if y_prob is not None:
    fpr, tpr, _ = roc_curve(y_test, y_prob)
    roc_auc = auc(fpr, tpr)
    plt.plot(fpr, tpr, label=f'{name} (AUC = {roc_auc:.2f}')
  plt.plot([0, 1], [0, 1], 'k--')
  plt.xlabel('False Positive Rate')
  plt.ylabel('True Positive Rate')
  plt.title('ROC Curve')
  plt.legend()
  plt.show()

# Data Visualization (Box Plot, Violin Plot, Scatter Plot, Pair Plot)
numeric_columns = ['Red Pixel', 'Blue pixel', 'Green pixel','Hb']

# Box Plot
plt.figure(figsize=(10, 6))
sns.boxplot(data=df[numeric_columns])
plt.title("Box Plot of Numeric Features")
plt.xticks(rotation=45)
plt.show()

# Violin Plot
plt.figure(figsize=(10, 6))
sns.violinplot(data=df[numeric_columns])
plt.title("Violin Plot of Numeric Features")
plt.xticks(rotation=45)
plt.show()

# Pair Plot
sns.pairplot(df, hue="Anaemic")
plt.show()



-----

from sklearn.linear_model import LinearRegression
import numpy as np

# Creating a sample dataset
data = {
    'SquareFeet': [1500, 1800, 2000, 1200, 2200],
    'Rooms': [3, 4, 4, 2, 5],
    'Price': [300000, 400000, 500000, 200000, 600000]
}

df = pd.DataFrame(data)

# Splitting into features and target
X = df[['SquareFeet', 'Rooms']]
y = df['Price']

# Train Linear Regression Model
model = LinearRegression()
model.fit(X, y)

# Predict on new data
new_house = np.array([[1600, 3]])  # 1600 sqft, 3 rooms
predicted_price = model.predict(new_house)
print("Predicted House Price:", predicted_price[0])

from sklearn.multioutput import MultiOutputRegressor

# Creating a sample dataset
data = {
    'StudyHours': [10, 8, 9, 7, 11],
    'SleepHours': [6, 7, 5, 6, 8],
    'PrevScore': [85, 80, 78, 72, 92],
    'MathScore': [90, 85, 80, 75, 95],
    'ScienceScore': [88, 82, 79, 70, 94]
}

df = pd.DataFrame(data)

# Splitting into features and target
X = df[['StudyHours', 'SleepHours', 'PrevScore']]
y = df[['MathScore', 'ScienceScore']]

# Train Multiple Regression Model
model = MultiOutputRegressor(LinearRegression())
model.fit(X, y)

# Predict on new student data
new_student = np.array([[9, 7, 85]])  # 9 study hours, 7 sleep hours, prev score 85
predicted_scores = model.predict(new_student)
print("Predicted Scores (Math, Science):", predicted_scores[0])

