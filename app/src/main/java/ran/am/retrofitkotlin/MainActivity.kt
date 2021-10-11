package ran.am.retrofitkotlin
import android.widget.*

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class MainActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {
    //lateinit var responseText: TextView
    lateinit var il: ArrayList<String>
    lateinit var ilv: ArrayList<Float>
    lateinit var key: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var datetxt = findViewById<View>(R.id.date) as TextView
        var uinput = findViewById<View>(R.id.uinput) as EditText
        var res = findViewById<View>(R.id.res) as TextView
        var btn = findViewById<View>(R.id.btn) as Button
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        var spinner = findViewById<Spinner>(R.id.spinner)

        val call: Call<BookDetails?>? = apiInterface!!.doGetListResources()
        ilv = ArrayList<Float>()
         il = ArrayList<String>()
        il.add(" ")

        btn.setOnClickListener(){
            var count = 0
            while (key!=il[count]){
                count++
            }
            res.text = "Result: ${ilv[count]*uinput.text.toString().toFloat()}"

        }

        call?.enqueue(object : Callback<BookDetails?> {
            override fun onResponse(
                call: Call<BookDetails?>?,
                response: Response<BookDetails?>
            ) {
                var displayResponse = ""
                val resource: BookDetails? = response.body()
                val currdic = resource?.eur
                val date = resource?.date

                for (curr in currdic!!) {
                    il.add(curr.key)
                    ilv.add(curr.value)

                    //displayResponse += """${datum.id.toString()} ${datum.name} ${datum.pantoneValue} ${datum.year}"""
                }
                il.removeAt(0)
                datetxt.text = datetxt.text.toString() +" "+ date
                //responseText.text = displayResponse
            }

            override fun onFailure(call: Call<BookDetails?>, t: Throwable?) {
                call.cancel()
            }
        })
        spinner!!.setOnItemSelectedListener(this)
        val array_adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item, il)
        array_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner!!.setAdapter(array_adapter)

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
        key = il[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}








//displayResponse += """${text.toString()} Page
//$total Total
//$totalPages Total Pages//"""