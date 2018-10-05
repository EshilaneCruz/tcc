package br.feevale.projetofinal

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_dehaze)

        navigationView.setNavigationItemSelectedListener (this)
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
                Toast.makeText(this, "Should see trips", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_update_profile -> {
                Toast.makeText(this, "Should see profile", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_meet_partners -> {
                Toast.makeText(this, "Should see partners", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_search_destinations -> {
                Toast.makeText(this, "Should see destinations", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_settings -> {
                Toast.makeText(this, "Should see settings", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_logout -> {
                Toast.makeText(this, "Should get out", Toast.LENGTH_SHORT).show()
            }
            else -> {return false}
        }
        drawerLayout.closeDrawers()
        return true
    }
}
