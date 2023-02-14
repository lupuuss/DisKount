import Foundation
import SwiftUI
import Shared

private struct ReduKtStoreKey: EnvironmentKey {
    typealias Value = ReduKtStore<AppState>
    
    static var defaultValue: ReduKtStore<AppState> = DisKountCore.shared.createStore()
}

extension EnvironmentValues {
    var reduKtStore: ReduKtStore<AppState> {
        get { self[ReduKtStoreKey.self] }
        set { self[ReduKtStoreKey.self] = newValue }
    }
}

class Observer<T> : ObservableObject {
    
    @Published private var rawValue: T? = nil
    var value: T { rawValue! }
    private var disposable: ReduKtDisposable? = nil
    
    init(_ subscribe: (@escaping (T) -> Void) -> ReduKtDisposable) {
        disposable = subscribe { [weak self] value in
            self?.rawValue = value
        }
    }
    deinit {
        disposable?.dispose()
    }
}

struct ObserveOn<State, Content>: View where Content: View {
    private let content: (State) -> Content
    @StateObject var observer: Observer<State>
    
    init(
        _ subscriber: @escaping (@escaping (State) -> Void) -> ReduKtDisposable,
        _ content: @escaping (State) -> Content
    ) {
        self.content = content
        self._observer = .init(wrappedValue: Observer(subscriber))
    }
    
    var body: some View {
        content(observer.value)
    }
}
