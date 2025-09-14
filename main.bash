# ==============================================================================
# SCRIPT UNTUK MENGGABUNGKAN PROYEK MENJADI SATU PROMPT
# ==============================================================================

# Nama file output akhir
OUTPUT_FILE="prompt_lengkap.txt"

# Langkah 1: Buat file baru dan sisipkan struktur folder di bagian atas.
# Opsi -I pada tree adalah untuk mengabaikan direktori/file.
echo "Struktur Proyek:" > $OUTPUT_FILE
tree -I 'node_modules|.git|dist|build|*.log' >> $OUTPUT_FILE
echo -e "\n\n========================================\n\nKODE SUMBER (Source Code):\n\n========================================\n" >> $OUTPUT_FILE

# Langkah 2: Cari semua file teks (abaikan gambar, dll.) dan gabungkan isinya.
find . -type f \
  -not -path "./.git/*" \
  -not -path "./node_modules/*" \
  -not -path "./dist/*" \
  -not -path "./build/*" \
  -not -name "$OUTPUT_FILE" \
  -not \( \
    -iname "*.png" -o -iname "*.jpg" -o -iname "*.jpeg" -o \
    -iname "*.gif" -o -iname "*.webp" -o -iname "*.svg" -o \
    -iname "*.ico" -o -iname "*.bmp" -o -iname "*.pdf" -o \
    -iname "*.zip" -o -iname "*.rar" -o -iname "*.mp3" -o -iname "*.mp4" \
  \) \
| while read -r file; do
    echo "--- File: $file ---" >> $OUTPUT_FILE
    cat "$file" >> $OUTPUT_FILE
    echo -e "\n" >> $OUTPUT_FILE
  done

echo "SELESAI! Prompt Anda telah dibuat di file: $OUTPUT_FILE"
