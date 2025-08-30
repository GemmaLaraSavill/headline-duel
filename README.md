# 📰 Headline Duel

**Headline Duel** is an Android app that connects a fine-tuned **DistilBERT news classifier** with a
mobile experience.  
The model was trained on the AG News dataset using **LoRA adapters** for efficient fine-tuning,
uploaded to **Hugging Face**, and exposed via an API.

This app lets users classify headlines themselves and compare their answers with the model’s
predictions - a fun, interactive way to validate and showcase the model.

## ✨ Features

- Classify real AG News headlines as *World, Sports, Business, or Sci/Tech*.
- Compare your answer with the prediction from a fine-tuned ML model.
- Powered by **Hugging Face Spaces API** for live inference.
- Built with **Jetpack Compose** and **Clean Architecture** in Kotlin.

## 🤖 Model Details

- Base model: [DistilBERT](https://huggingface.co/distilbert-base-uncased)
- Dataset: [AG News](https://huggingface.co/datasets/sh0416/ag_news)
- Fine-tuning: Parameter-efficient tuning with **LoRA adapters**
- Deployment: Hosted on Hugging Face at
    -
    Model: [gemmalarasav/distilbert-ag-news-lora-merged](https://huggingface.co/gemmalarasav/distilbert-ag-news-lora-merged)
    - Space
      API: [news-classifier-space](https://huggingface.co/spaces/gemmalarasav/news-classifier-space)

## 🛠 Tech Stack

- **Android**: Kotlin, Jetpack Compose, Coroutines, Clean Architecture, Ktor
- **ML/AI**: Hugging Face Transformers, LoRA, Hugging Face Spaces

## 🚀 How It Works

1. User is shown a headline and selects what category it belongs to.
2. The same headline is sent to the deployed Hugging Face model.
3. App compares user’s choice with the model prediction.
4. Feedback is displayed interactively.

## 📚 Learning Context

This project started as part of my **Udacity Generative AI Nanodegree** to fine-tune a model.  
By building this app, my aim is to connect my work in **machine learning** with my experience in **Android
development**  and bring the model into a practical, real-world test scenario.

## 👩‍💻 Author

Developed by [Gemma Lara Savill](https://www.myhappyplace.dev).  
Sharing AI + Android experiments on [GitHub](https://github.com/gemmalarasav).