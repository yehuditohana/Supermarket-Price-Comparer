import pandas as pd
import numpy as np
import joblib
import sys

# Load arguments
input_csv = sys.argv[1]
output_csv = sys.argv[2]

# Load model
model_path = sys.argv[3]
model = joblib.load(model_path)

# Load products to classify (with item_id and item_name)
df = pd.read_csv(input_csv, encoding='utf-8')
df['item_name'] = df['item_name'].fillna('')

# Predict probabilities and labels
probas = model.predict_proba(df['item_name'])
predictions = model.predict(df['item_name'])
max_probas = np.max(probas, axis=1)

# Set confidence threshold
threshold = 0.05
df['predicted_combined_category'] = [
    pred if prob >= threshold else "Unknown ||| Unknown ||| Unknown"
    for pred, prob in zip(predictions, max_probas)
]

# Split into category columns
df[['general_category', 'sub_category', 'specific_category']] = df['predicted_combined_category'].str.split(r' \|\|\| ', expand=True)

# Save only the needed columns, matching the expected Java format
df[['item_id', 'general_category', 'sub_category', 'specific_category']].to_csv(output_csv, index=False, encoding='utf-8-sig')

print("Done! Saved with predicted categories.")