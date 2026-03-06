🚑 Hayat AI (حياة)

"Providing life-saving medical guidance where there is no doctor."

Hayat AI is an offline-first, mobile medical assistant designed for high-conflict and crisis zones like Gaza and Kashmir. It provides physician-validated emergency protocols (trauma, surgery, first aid) without requiring an internet connection.

🌟 Mission Statement

In crisis zones, hospitals are often destroyed and internet is cut off. Hayat AI aims to put the collective knowledge of the ICRC, WHO, and field surgeons into the pocket of every civilian, enabling them to stabilize injuries and save lives when professional help is unavailable.

🛠️ Technical Architecture

1. The Brain (The Model)

Base Model: Qwen2.5-1.5B-Medical-Finetuned

Optimization: Compressed via ZipNN or GGUF quantization to ~800MB–1.1GB to ensure it runs on entry-level Android devices (e.g., Samsung A-series).

Fine-Tuning: LoRA adaptation based on "Austerity Medicine" (medical care with limited supplies).

2. The Library (RAG System)

Sources: ICRC War Surgery Manuals, WHO Emergency Guidelines, and University-level Trauma Courses.

Format: Retrieval-Augmented Generation (RAG) using a local vector database to ensure the AI never "hallucinates" and always cites a specific medical book.

3. The App (The Shell)

Framework: Built on the PocketPal AI open-source engine.

Core Feature: 100% Offline functionality.

Sharing: Integrated Peer-to-Peer (P2P) sharing via Bluetooth and WiFi Direct.

🩺 Medical Dataset Strategy (The "Awais-Ibtisam" Loop)

To ensure 100% safety, we do not trust raw AI translations. We use a Triple-Check Validation Loop:



Selection: Dr. Awais selects high-priority English medical protocols (Bleeding, Blast Trauma, Burns).

AI Drafting: ChatGPT/Claude drafts a "Simple Arabic" version for civilians.

Physician Review: Dr. Awais and Ibtisam verify the translation by "Back-Translating" it to English to ensure no medical dosage or step was lost.

Final Approval: Only "Doctor-Approved" rows are added to the Master Dataset.

📡 Distribution Plan (Gaza/Kashmir)

Since the Play Store is often inaccessible, Hayat AI will spread via:



The Mesh Network: A built-in "Share App" button using Bluetooth/WiFi Direct to spread the APK from phone to phone.

NGO Partners: Initial "Seed" drops via humanitarian organizations and field hospitals.

Hard Copy Backup: A "Print-Ready" feature that generates 1-page Arabic emergency cheat sheets for physical distribution.

📅 4-Week Project Roadmap

Week 1: Data Acquisition & Foundation

Dr. Awais: Finalize collection of English ICRC/WHO PDFs and University Emergency manuals.

Ubaid: Set up the PocketPal dev environment and remove non-medical features.

Bazeem: Download and test the Qwen2.5-1.5B medical model for speed/latency.

Week 2: The Translation & Fine-Tuning

Medical Team: Complete the first 50 "Critical Emergency" Q&A pairs in the Google Sheet (The Triple-Check Loop).

Dev Team: Integrate the RAG system so the AI can "read" the English manuals.

Bazeem: Begin lightweight LoRA fine-tuning with the first batch of verified data.

Week 3: Integration & UI/UX

Ubaid: Build the Arabic UI and the "Share via Bluetooth" module.

Medical Team: Complete the remaining 50–100 scenarios (Dehydration, Childbirth, Infections).

Testing: Alpha testing on low-end Android devices to check battery heat and RAM usage.

Week 4: Deployment & Outreach

Final Review: Dr. Awais signs off on the full medical library.

Optimization: Final compression of the model to its smallest possible size.

Launch: Share the APK with initial NGO contacts for delivery to Gaza.

👥 The Team

Ubaid Ur Rehman: Project Lead & App Developer.

Dr. Awais (MBBS): Chief Medical Officer (Dataset & Safety).

Bazeem: ML Engineer (Model Optimization & Fine-tuning).

Ibtisam: Medical Reviewer & Quality Control.

⚠️ Medical Disclaimer

Hayat AI is an emergency assistance tool for use when professional medical care is unavailable. It is intended to support, not replace, the judgment of healthcare professionals. All protocols are based on ICRC and WHO "Austerity Medicine" guidelines.

"Whosoever saves a life, it is as if he has saved the whole of mankind." (5:32)
