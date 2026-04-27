import { useAuth } from '../context/AuthContext';
import { Coffee, LogOut, TrendingUp } from 'lucide-react';

export default function Dashboard() {
  const { user, signOut } = useAuth();

  return (
    <div className="min-h-screen bg-gray-50">
      <nav className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center">
              <Coffee className="w-8 h-8 text-primary-500" />
              <span className="ml-2 text-xl font-bold text-gray-900">Café Forecast</span>
            </div>
            <div className="flex items-center space-x-4">
              <span className="text-gray-600">Olá, {user?.username}</span>
              <button
                onClick={signOut}
                className="flex items-center text-gray-600 hover:text-gray-900"
              >
                <LogOut className="w-5 h-5 mr-1" />
                Sair
              </button>
            </div>
          </div>
        </div>
      </nav>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <h1 className="text-2xl font-bold text-gray-900 mb-6">Dashboard</h1>
        
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div className="bg-white p-6 rounded-lg shadow-sm">
            <div className="flex items-center">
              <Coffee className="w-8 h-8 text-primary-500" />
              <div className="ml-4">
                <p className="text-sm text-gray-600">Produtos</p>
                <p className="text-2xl font-bold text-gray-900">Em breve</p>
              </div>
            </div>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-sm">
            <div className="flex items-center">
              <TrendingUp className="w-8 h-8 text-green-500" />
              <div className="ml-4">
                <p className="text-sm text-gray-600">Vendas</p>
                <p className="text-2xl font-bold text-gray-900">Em breve</p>
              </div>
            </div>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-sm">
            <div className="flex items-center">
              <Coffee className="w-8 h-8 text-blue-500" />
              <div className="ml-4">
                <p className="text-sm text-gray-600">Previsão IA</p>
                <p className="text-2xl font-bold text-gray-900">Em breve</p>
              </div>
            </div>
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-sm">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Bem-vindo ao Café Forecast!</h2>
          <p className="text-gray-600">
            Este sistema utiliza Inteligência Artificial para prever a demanda de produtos 
            e sugerir quantidades de produção diária.
          </p>
        </div>
      </main>
    </div>
  );
}
