import pandas as pd
import re
import random
import joblib
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import Pipeline
from sklearn.metrics import accuracy_score, classification_report

# Function to clean text by removing digits, punctuation, and extra spaces
def clean_text(text):
    text = str(text).lower()
    text = re.sub(r'\d+', '', text)
    text = text.replace('.', ' ')
    text = re.sub(r'[^\w\s]', '', text)
    text = re.sub(r'\s+', ' ', text).strip()
    return text

# Function to randomly augment text by dropping or shuffling words
def augment_text(text):
    words = text.split()
    if len(words) < 2:
        return text
    op = random.choice(["drop", "shuffle", "none"])
    if op == "drop":
        words.pop(random.randint(0, len(words) - 1))
    elif op == "shuffle":
        random.shuffle(words)
    return ' '.join(words)

# Load and preprocess the dataset
df = pd.read_csv("tagged_products.csv")

# Data augmentation: create multiple noisy versions of the dataset
augmented = []
for _ in range(10):
    temp = df.copy()
    temp['item_name'] = temp['item_name'].fillna('').apply(clean_text).apply(augment_text)
    augmented.append(temp)

# Combine all augmented datasets into a single dataframe
df = pd.concat(augmented, ignore_index=True)

# Create a combined label from category hierarchy
df['combined_category'] = df['category'] + " ||| " + df['subcategory'] + " ||| " + df['specific_category']

# Split the data into training and testing sets
X_train, X_test, y_train, y_test = train_test_split(
    df['item_name'], df['combined_category'], test_size=0.2, random_state=42
)

# Build a pipeline with TF-IDF vectorizer and Logistic Regression
model = Pipeline([
    ('tfidf', TfidfVectorizer(max_features=1200000, ngram_range=(1, 4))),
    ('clf', LogisticRegression(max_iter=120000))
])

# Train the model
model.fit(X_train, y_train)

## Evaluate the model
y_pred = model.predict(X_test)
accuracy = accuracy_score(y_test, y_pred)
report = classification_report(y_test, y_pred)

print("Model trained and saved!")
print(f"Accuracy: {accuracy:.4f}")
print(" Classification Report:")
print(report)

# Save the trained model to a file
joblib.dump(model, "product_classifier.pkl")