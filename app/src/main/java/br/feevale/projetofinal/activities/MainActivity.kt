package br.feevale.projetofinal.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import android.widget.Toast
import br.feevale.projetofinal.R
import br.feevale.projetofinal.models.TripResume
import br.feevale.projetofinal.services.SharedPreferencesService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_dehaze)
        navigationView.setNavigationItemSelectedListener (this)
        new_trip_button.setOnClickListener { goToStartTrip() }
        action_manage_trips_main.setOnClickListener { goToPresentTrips() }
        action_news_main.setOnClickListener { goToPresentNews() }
        action_search_partners_main.setOnClickListener { goToPresentPartners() }
        action_search_destinations_main.setOnClickListener { goToPresentDestinations() }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_manage_trips -> {
                goToPresentTrips()
            }
            R.id.nav_meet_partners -> {
                goToPresentPartners()
            }
            R.id.nav_search_destinations -> {
                goToPresentDestinations()
            }
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                SharedPreferencesService.write("AuthenticatedUser", "OFF")
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            else -> {return false}
        }
        drawerLayout.closeDrawers()
        return true
    }
    private fun goToStartTrip(){
        startActivity( Intent(this, StartTripActivity::class.java))
    }
    private fun goToPresentTrips(){
        startActivity( Intent(this, PresentTripActivity::class.java))
    }
    private fun goToPresentNews(){
        startActivity( Intent(this, PresentNewsPromoActivity::class.java))
    }
    private fun goToPresentPartners(){
        startActivity( Intent(this, PresentPartnersActivity::class.java))
    }
    private fun goToPresentDestinations(){
        startActivity( Intent(this, PresentDestinationsActivity::class.java))
    }
}
