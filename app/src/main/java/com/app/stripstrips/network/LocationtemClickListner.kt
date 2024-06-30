package com.app.stripstrips.network

import com.app.stripstrips.model.LatlongApiModel.PredictionsItem

interface LocationtemClickListner {
 fun onItemClickListner(mPredictionsItem: PredictionsItem)
}