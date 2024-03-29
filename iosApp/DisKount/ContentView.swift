import Foundation
import SwiftUI
import Shared


struct ContentView: View {
    
    @Environment(\.reduKtStore) var store
    
    var body: some View {
        ObserveOn(store.subscribeNavigation(onChange:)) { navigation in
            switch navigation.last?.type {
            case is DestinationType.Splash: SplashView()
            case is DestinationType.AllDeals: DealsView()
            case let details as DestinationType.DealDetails: DealDetailsView(id: details.id)
            default: EmptyView()
            }
        }
    }
}
