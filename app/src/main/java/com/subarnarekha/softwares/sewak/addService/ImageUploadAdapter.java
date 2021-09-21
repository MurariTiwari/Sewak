package com.subarnarekha.softwares.sewak.addService;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.subarnarekha.softwares.sewak.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ImageUploadAdapter extends RecyclerView.Adapter<ImageUploadAdapter.MyViewHolder> {
    List<String> fileName,status;
    List<Uri> imageUri;
    public ImageUploadAdapter(List<String> fileName, List<String> status,List<Uri> imageUri) {
        this.fileName = fileName;
        this.status = status;
        this.imageUri = imageUri;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_upload,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageUploadAdapter.MyViewHolder holder, int position) {

        String file = fileName.get(position);
        holder.fileName.setText(file);
        holder.imageIcon.setImageURI(imageUri.get(position));
        String fileStatus = status.get(position);
        if(fileStatus.equals("loading")){
            holder.statusSpinner.setVisibility(View.VISIBLE);
        }else{
            holder.statusSpinner.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return fileName.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageIcon;
        TextView fileName;
        ProgressBar statusSpinner;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageIcon = itemView.findViewById(R.id.image_upload_icon);
            fileName = itemView.findViewById(R.id.filename);
            statusSpinner = itemView.findViewById(R.id.status_spinner);

        }
    }
}
