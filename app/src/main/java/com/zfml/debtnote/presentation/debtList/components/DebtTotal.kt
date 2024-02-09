package com.zfml.debtnote.presentation.debtList.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zfml.debtnote.domain.model.Debt
import com.zfml.debtnote.presentation.debtList.list

@Composable
fun DebtTotal(
    debts: List<Debt>
) {
    
   var debtsToPay = 0.0
   var debtsToGet = 0.0
   
   for(debt in debts) {
       if(debt.oweMe) {
           debtsToGet += debt.amount
       } else {
           debtsToPay += debt.amount
       }
   }
    
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp)
        ,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        if(debtsToGet > 0) {
            Column() {
                Text(
                    text = "Total To Get",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "$debtsToGet",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        if(debtsToPay > 0) {
            Column(
                modifier =Modifier.padding(end = 16.dp)
            ) {
                Text(
                    text = "Total To Pay",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "$debtsToPay",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun DebtTotalPreview() {
    DebtTotal(debts = list)
}