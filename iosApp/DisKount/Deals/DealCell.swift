import Foundation
import SwiftUI
import Shared
import Kingfisher

struct DealCell : View {
    
    var deal: DealItem
    
    var body: some View {
        VStack(alignment: .leading) {
            Text(deal.title)
                .font(.title2)
                .lineLimit(1)
            HStack {
                KFImage(URL(string: deal.thumbUrl))
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(height: 200)
                    .frame(maxWidth: .infinity)
                    .background(Color.white)
                    .clipShape(RoundedRectangle(cornerRadius: 20))
                Spacer(minLength: 8)
                VStack {
                    HStack {
                        Text("\(deal.normalPrice.description)$")
                            .fontWeight(.light)
                            .strikethrough()
                        Text("\(deal.salePrice.description)$")
                            .bold()
                    }.labelCard()
                    Spacer()
                    VStack {
                        Text("Metascore").font(.caption)
                        Text(deal.metacriticScore?.description ?? "N/A").bold()
                    }.labelCard()
                    Spacer()
                    VStack {
                        Text("Steam rating").font(.caption)
                        if let steamRating = deal.steamRatingPercent {
                            Text("\(steamRating)%").bold()
                        } else {
                            Text("N/A").bold()
                        }
                    }.labelCard()
                }.frame(maxWidth: .infinity)
            }
        }
        .padding(16)
        .background(Color.gray.cornerRadius(20))
    }
}

fileprivate extension View {
    func labelCard() -> some View {
        frame(maxWidth: .infinity)
            .padding(8)
            .background(Color.white.cornerRadius(10))
    }
}
