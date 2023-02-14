import Foundation
import SwiftUI
import Shared


struct ContentView: View {
    
    @Environment(\.reduKtStore) var store
    
    var body: some View {
        ObserveOn(store.subscribeNavigation(onChange:)) { navigation in
            Text(navigation.description)
        }
    }
}
