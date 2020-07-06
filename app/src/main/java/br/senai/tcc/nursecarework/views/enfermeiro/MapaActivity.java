package br.senai.tcc.nursecarework.views.enfermeiro;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.senai.tcc.nursecarework.R;
import br.senai.tcc.nursecarework.models.Requisicao;

public class MapaActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        Requisicao requisicao = (Requisicao) getIntent().getSerializableExtra("Requisicao");
        latLng = new LatLng(requisicao.getLatitude(), requisicao.getLongitude());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(latLng).title("Paciente"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
    }
}
