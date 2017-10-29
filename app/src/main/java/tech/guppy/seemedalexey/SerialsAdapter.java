package tech.guppy.seemedalexey;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by serych on 19.10.2017.
 */

public class SerialsAdapter extends RecyclerView.Adapter<SerialsAdapter.SerialsViewHolder> {

    private List<Serial> serials = new ArrayList<>();
    private Context context;

    public SerialsAdapter(Context context) {
        super();
        this.context = context;
    }


    @Override
    public SerialsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.serial_item, parent, false);
        return new SerialsViewHolder(view);
    }

    @Override
    public void onViewRecycled(SerialsViewHolder holder) {
        super.onViewRecycled(holder);
        Picasso.with(context).cancelRequest(holder.preview);
    }

    @Override
    public void onBindViewHolder(final SerialsViewHolder holder, int position) {

        final Serial serial = serials.get(position);


        // If the focus is changed, highlight serial (or updater item)
        holder.background.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        holder.background.setBackgroundColor(context.getResources().getColor(R.color.colorHighlight, null));
                    }
                    else {
                        holder.background.setBackgroundColor(context.getResources().getColor(R.color.colorHighlight));
                    }
                } else {
                    holder.background.setBackground(null);
                }
            }
        });


        if (serial == null) {
            // Show updater button

            holder.pb.setVisibility(View.VISIBLE);
            holder.notpb.setVisibility(View.GONE);
            holder.background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(MainActivity.ACTION_UPDATE_LIST));
                }
            });

        } else {

            // Show serial data
            holder.pb.setVisibility(View.GONE);
            holder.notpb.setVisibility(View.VISIBLE);

            // Set serial name
            holder.name.setText(serial.getName());

            // Set serial info
            holder.info.setText("s" + serial.getSeason() + "e" + serial.getEpisode());

            // Set serial image with Picasso
            holder.preview.setImageBitmap(null);
            try {
                Picasso.with(context).load(serial.getPrev()).into(holder.preview);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Set the star picture if the serial is favourite
            if (serial.getFavorite().equals("1")) {
                holder.star.setImageResource(android.R.drawable.star_big_on);
            } else {
                holder.star.setImageResource(android.R.drawable.star_big_off);
            }

            // Set the flag to show the language
            switch (serial.getLang()) {
                case "RUS":
                    holder.lang.setImageResource(R.drawable.flag_ru);
                    break;
                case "ENG":
                    holder.lang.setImageResource(R.drawable.flag_us);
                    break;
                default:
                    holder.lang.setImageResource(0);
            }

            // Set click listner on background, which will open video in some player
            holder.background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(serial.getVideolink()));
                    intent.setDataAndType(Uri.parse(serial.getVideolink()), "video/mp4");
                    context.startActivity(intent);
                }
            });
        }
    }

    public void setSerials(List<Serial> serials) {
        this.serials = serials;
    }

    @Override
    public int getItemCount() {
        return serials.size();
    }

    class SerialsViewHolder extends RecyclerView.ViewHolder {

        ImageView preview;
        TextView name;
        TextView info;
        ImageView star;
        ImageView lang;
        View base;
        View background;


        /**
         * ProgressBar for the updater
         */
        View pb;

        /**
         * All the other views
         */
        View notpb;


        public SerialsViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            preview = itemView.findViewById(R.id.preview);
            base = itemView;
            info = itemView.findViewById(R.id.bottom_info);
            star = itemView.findViewById(R.id.star);
            lang = itemView.findViewById(R.id.lang);
            background = itemView.findViewById(R.id.background);

            pb = itemView.findViewById(R.id.pb);
            notpb = itemView.findViewById(R.id.notpb);
        }
    }
}
