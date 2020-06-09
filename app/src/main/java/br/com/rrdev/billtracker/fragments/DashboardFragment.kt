package br.com.rrdev.billtracker.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.rrdev.billtracker.R
import br.com.rrdev.billtracker.activities.LoginActivity
import br.com.rrdev.billtracker.models.Despesa
import br.com.rrdev.billtracker.models.Registro
import br.com.rrdev.billtracker.models.User
import br.com.rrdev.billtracker.models.states.Complete
import br.com.rrdev.billtracker.models.states.CompleteWithNoData
import br.com.rrdev.billtracker.models.states.Error
import br.com.rrdev.billtracker.models.states.Loading
import br.com.rrdev.billtracker.models.states.UIState
import br.com.rrdev.billtracker.repositories.FirebaseCollection
import br.com.rrdev.billtracker.utils.CharXAxisFormatter
import br.com.rrdev.billtracker.utils.convertTimestampToDate
import br.com.rrdev.billtracker.utils.formatOnPattern
import br.com.rrdev.billtracker.viewmodel.MainViewModel
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_registro.*

class DashboardFragment: Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.uiState.observe(viewLifecycleOwner, Observer(this::handleUIStateForChart))

        viewModel.getRegistrosFromFireStore()

        val user = lastSignInAccount()
        user?.let {
            text_name_user.text = user.name
            Glide.with(requireActivity()).load(user.photoUrl).into(igm_perfil)
        }

        button_logout.setOnClickListener {
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build()
            val googleSignClient = GoogleSignIn.getClient(requireActivity(), gso)
            googleSignClient.signOut().addOnCompleteListener {
                startActivity(Intent(context, LoginActivity::class.java))
                activity?.finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getRegistrosFromFireStore()
    }

    private fun handleUIStateForChart(uiState: UIState){

        when(uiState){
            is Loading ->{
                graphic_chart.visibility = View.GONE
                load_content_dashboard.visibility = View.VISIBLE
                empty_content_dashboard.visibility = View.GONE
            }

            is CompleteWithNoData ->{
                graphic_chart.visibility = View.GONE
                load_content_dashboard.visibility = View.GONE
                empty_content_dashboard.visibility = View.VISIBLE
            }
            is Complete<*>->{
                val entries = (uiState.data as List<Despesa>).map {
                    Entry(it.data.toFloat(), it.valor.toFloat())
                }
                val labels = entries.map {
                    convertTimestampToDate(it.x.toLong()).formatOnPattern("dd/MM/yyyy")
                }
                val lineDataSet = LineDataSet(entries, "despesas")
                lineDataSet.fillColor = context?.getColor(R.color.colorIndicatorBarDespesa) ?: Color.RED
                val dataSets = mutableListOf<ILineDataSet>(lineDataSet)

                graphic_chart.xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    valueFormatter = CharXAxisFormatter()
                }

                graphic_chart.data = LineData(dataSets)
                graphic_chart.visibility = View.VISIBLE

                load_content_dashboard.visibility = View.GONE
                empty_content_dashboard.visibility = View.GONE
//                graphic_chart.isDragEnabled = true
//                graphic_chart.setScaleEnabled(true)
            }
            is Error ->{
                graphic_chart.visibility = View.GONE
                load_content_dashboard.visibility = View.GONE
                empty_content_dashboard.visibility = View.VISIBLE
                Toast.makeText(context, getString(uiState.errorMsgId), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun lastSignInAccount(): User? {
        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())

        return if (acct != null) {
            User(id=acct.id, name = acct.displayName, email = acct.email, photoUrl = acct.photoUrl)
        } else null
    }
}