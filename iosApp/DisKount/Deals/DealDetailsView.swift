import Foundation
import SwiftUI
import Shared

struct DealDetailsView : View {
    
    let id: Deal.Id
    @Environment(\.reduKtStore) var store
    
    var body: some View {
        ObserveOn({ onChange in store.subscribeDeal(id: id, onChange: onChange) }) { deal in
            Text(deal?.description() ?? "")
                .onTapGesture { store.dispatch(action: NavigationActionPop.shared) }
        }
    }
}
