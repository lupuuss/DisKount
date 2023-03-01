import Foundation
import SwiftUI
import Shared

struct DealsView: View {
    
    @Environment(\.reduKtStore) var store
        
    var body: some View {
        ObserveOn(store.subscribeDeals(onChange:)) { deals in
            HStack(alignment: .center) {
                Text("Discounts")
                    .font(.title)
                Spacer()
                Button(action: { store.dispatch(action: DealAction.LoadMore(invalidate: true)) }) {
                    Image(systemName: "arrow.clockwise")
                }
            }
            .padding(.horizontal, 16)
            ScrollView {
                LazyVStack {
                    let items = deals.data as! [DealItem]
                    ForEach(items) { deal in
                        DealCell(deal: deal)
                            .onTapGesture { store.dispatch(action: DealAction.GoToDetails(id: deal.id)) }
                            .onAppear {
                                if items.last == deal && deals.hasMore {
                                    store.dispatch(action: DealAction.LoadMore(invalidate: false))
                                }
                            }
                    }
                    if deals.isLoading {
                        ProgressView()
                    }
                }
                .padding(16)
            }
        }
        .onAppear { store.dispatch(action: DealAction.LoadMore(invalidate: false)) }
    }
}

extension DealItem : Identifiable { }
